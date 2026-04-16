package wishlistgr6.wishlist.repository.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WishRowMapper implements RowMapper<Wish> {

    @Override
    public Wish mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Wish foundWish = new Wish(
                resultSet.getString("wish_name"),
                resultSet.getString("description"),
                resultSet.getString("product_url"),
                resultSet.getString("comments"),
                resultSet.getDouble("price"),
                resultSet.getBoolean("isReserved")
        );
        return addEvents(resultSet, foundWish);
    }

    private Wish addEvents(ResultSet resultSet, Wish wish) throws SQLException{
        do{
            wish.addEvent(new Event(
                    resultSet.getString("event_name"),
                    resultSet.getDate("event_date")));
        }while(resultSet.next() && wish.getName().equals(resultSet.getString("wish_name")));
        return wish;
    }

}
