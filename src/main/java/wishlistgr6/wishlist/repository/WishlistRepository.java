package wishlistgr6.wishlist.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;
import wishlistgr6.wishlist.exceptions.WishNotFoundException;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.rowMappers.WishlistRowMapper;

import java.sql.*;
import java.util.List;
import java.util.Random;

@Repository
public class WishlistRepository {
    private final JdbcTemplate jdbcTemplate;

    public WishlistRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    private String generateAccessToken(){

        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 8;

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateShareLink(String listID) {
        List<String> existing = getAccessTokens(listID);
        if (!existing.isEmpty()) {
            return "http://localhost:8080/login/" + listID + "?accessToken=" + existing.getFirst();
        }
        String accesstoken = generateAccessToken();
        String SQL = "insert into access_token(token, listID) values(?, ?);";
        jdbcTemplate.update(SQL, accesstoken, listID);
        return "http://localhost:8080/login/" + listID + "?accessToken=" + accesstoken;
    }




    public List<String> getAccessTokens(String listID){
        String sqlAccessToken =
                "select access_token.token as token " +
                "from wishlist join access_token " +
                "on wishlist.listID = access_token.listID " +
                "and wishlist.listID = ?";

        return jdbcTemplate.query(sqlAccessToken, new SingleColumnRowMapper<>(), listID);
    }

    public Wishlist getWishlist(String listID){
        String sqlWishlist = "select wishlist.listID as listID, " +
                "wishlist.list_name as list_name " +
                "from wishlist where listID = ?";

        return jdbcTemplate.query(sqlWishlist, new WishlistRowMapper(jdbcTemplate), listID).getFirst();
    }

    public boolean checkOwnerPassword(String listID, String password){
        String SQLPassword = "select ownerPW from wishlist where listID = ?";

        return jdbcTemplate.query(SQLPassword, new SingleColumnRowMapper<>(), listID).getFirst().equals(password);
    }
    public boolean checkGuestPassword(String listID, String password){
        String SQLPassword = "select guestPW from wishlist where wishlist.listID = ?";

        return jdbcTemplate.query(SQLPassword, new SingleColumnRowMapper<>(), listID).getFirst().equals(password);
    }

    public boolean addWish(Wish wish, String listID) {
        String sqlWish = "insert into wish(listID, wish_name, description, product_url, comments, price, isReserved) " +
                "values(?, ?, ?, ?, ?, ?, false)";

        String sqlEvent = String.format("insert into event_wish(wishID, eventID) " +
                "select w.wishID, e.eventID " +
                "from (select wishID from wish " +
                "where wish_name = '%s') as w " +
                "cross join (select eventID from event " +
                "where event_name in (%s)) as e ", wish.getName(), formatEvents(wish.getEvents()));

        jdbcTemplate.update(sqlWish, listID, wish.getName(), wish.getDescription(), wish.getProductURL(),
                wish.getComments(), wish.getPrice());
        jdbcTemplate.update(sqlEvent);
        return true;
    }

    public String createWishlist(Wishlist newWishlist, String ownerPassword, String guestPassword) throws SQLException{
        String generatedListId = generateAccessToken();
        String sqlWishlist = """
                insert into wishlist (listID, list_name, ownerPW, guestPW)
                values (?, ?, ?, ?);
                """;
        String sqlNoEvents = """
                insert into event (listID, event_name, event_date)
                values (?, ?, ?);
                """;
        jdbcTemplate.update(sqlWishlist, preparedStatement -> {
            preparedStatement.setString(1, generatedListId);
            preparedStatement.setString(2, newWishlist.getName());
            preparedStatement.setString(3, ownerPassword);
            preparedStatement.setString(4, guestPassword);
        });
        jdbcTemplate.update(sqlNoEvents, preparedStatement -> {
            Event event = newWishlist.getEvents().getFirst();
            preparedStatement.setString(1, generatedListId);
            preparedStatement.setString(2, event.title());
            preparedStatement.setString(3, event.date().toString());
        });
        return generatedListId;
    }



    public void updateWish(Wish wish, String listId, String originalWishName) {
        String sqlWish =
                """
                update wish
                set wish_name = ?, description = ?, product_url = ?, comments = ?, price = ?, isReserved = ?, image = ? where listID = ? and wish_name = ?
               """;
        jdbcTemplate.update(
                sqlWish,
                wish.getName(), wish.getDescription(), wish.getProductURL(),
                wish.getComments(), wish.getPrice(), wish.isReserved(),
                wish.getImage(), listId, originalWishName);
    }

    private String formatEvents(List<Event> events){
        if(events.isEmpty()){
            return "'No event'";
        }
        StringBuilder formatted = new StringBuilder("'" + events.getFirst().title() + "'");
        if(events.size()>1){
            for (int i = 1; i< events.size(); i++){
                formatted.append(", '").append(events.get(i).title()).append("'");
            }
        }
        return formatted.toString();
    }

    public void deleteWish(Wish wish){
        String sqlJoinDelete =
                """
                delete from event_wish where wishID = (
                       select wishID from wish where wish_name = ?
                )
                """;
        String sqlDelete =
                """
                        delete from wish where wish_name = ?
                        """;

        jdbcTemplate.update(sqlJoinDelete, preparedStatement ->{
           preparedStatement.setString(1, wish.getName());
        });
        int affectedRows = jdbcTemplate.update(sqlDelete, preparedStatement -> {
            preparedStatement.setString(1, wish.getName());
        });
        if(affectedRows==0){
            throw new WishNotFoundException();
        }

    }


}
