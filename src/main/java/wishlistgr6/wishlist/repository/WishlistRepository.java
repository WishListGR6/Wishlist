package wishlistgr6.wishlist.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.rowMappers.WishlistRowMapper;

import java.sql.ResultSet;
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
                "select access_token.token as token" +
                "from wishlist join access_token " +
                "on wishlist.listID = access_token.listID" +
                "and wishlist.listID = ?";

        RowMapper<String> rowMapper = ResultSet::getString;
        return jdbcTemplate.query(sqlAccessToken, rowMapper, listID);
    }

    public Wishlist getWishlist(String listID){
        String sqlWishlist = "select wishlist.listID as listID, " +
                "wishlist.wish_name as list_name" +
                "where wishlist.listID = ?";

        return jdbcTemplate.query(sqlWishlist, new WishlistRowMapper(), listID).getFirst();
    }

    public boolean checkOwnerPassword(String listID, String password){
        String SQLPassword = "select ownerPW from wishlist where wishlist.listID = ?";

        RowMapper<String> rowMapper = ResultSet::getString;
        return jdbcTemplate.query(SQLPassword, rowMapper, listID).getFirst().equals(password);
    }
    public boolean checkGuestPassword(String listID, String password){
        String SQLPassword = "select guestPW from wishlist where wishlist.listID = ?";

        RowMapper<String> rowMapper = ResultSet::getString;
        return jdbcTemplate.query(SQLPassword, rowMapper, listID).getFirst().equals(password);
    }

    public String addWish(Wish wish) {
        String sqlWish =
                "";

        return jdbcTemplate.query(sqlWish, rowMapper);
    }
}
