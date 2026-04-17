package wishlistgr6.wishlist.repository.rowMappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishRowMapper implements ResultSetExtractor<List<Wish>> {

    @Override
    public List<Wish> extractData(ResultSet resultSet) throws SQLException {
        List<Wish> foundWishes = new ArrayList<>();
        Wish foundWish = new Wish();
        while(resultSet.next()){
            if (foundWish.getName() == null ||!foundWish.getName().equals(resultSet.getString("wish_name"))){
                foundWish = new Wish(
                        resultSet.getString("wish_name"),
                        resultSet.getString("description"),
                        resultSet.getString("product_url"),
                        resultSet.getString("comments"),
                        resultSet.getDouble("price"),
                        resultSet.getBoolean("isReserved")
                );
                foundWishes.add(foundWish);
            }
            foundWishes.getLast().addEvent(new Event(
                    resultSet.getString("event_name"),
                    resultSet.getDate("event_date")));
        }

        return foundWishes;
    }

}
