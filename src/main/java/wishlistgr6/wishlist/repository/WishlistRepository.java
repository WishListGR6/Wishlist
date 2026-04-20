package wishlistgr6.wishlist.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.rowMappers.WishlistRowMapper;

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

    public String addWish(Wish wish, String listID) {
        String sqlWish =
                "insert into wish(listID, wish_name, description, product_url, comments, price, isReserved) " +
                        "values(?, ?, ?, ?, ?, ?, false)";

        String sqlEvent = String.format("insert into event_wish(eventID, wishID) " +
                "select e.eventID, w.wishID " +
                "from(select eventID from event " +
                "where event_name = '%s') as e " +
                "cross join(select wishID from wish " +
                "where wish_name = '%s') as w", formatEvents(wish.getEvents()), wish.getName());

        jdbcTemplate.update(sqlWish, listID, wish.getName(), wish.getDescription(), wish.getProductURL(),
                wish.getComments(), wish.getPrice());
        jdbcTemplate.update(sqlEvent);
        return "Wish added";
    }

    private String formatEvents(List<Event> events){
        if(events.isEmpty()){
            return "";
        }
        StringBuilder formatted = new StringBuilder("'" + events.getFirst() + "'");
        if(events.size()>1){
            for (int i = 1; i< events.size(); i++){
                formatted.append(", '").append(events.get(i)).append("'");
            }
        }
        return formatted.toString();
    }
}
