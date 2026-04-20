package wishlistgr6.wishlist.repository.rowMappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WishlistRowMapper implements RowMapper<Wishlist> {
    private final JdbcTemplate jdbcTemplate;

    public WishlistRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Wishlist mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        List<Wish> wishes = findWishes(resultSet.getString("listID"));
        List<Event> events = findEvents(resultSet.getString("listID"));

        return new Wishlist(
                resultSet.getString("list_name"),
                wishes,
                events
        );
    }

    private List<Wish> findWishes(String listID){
        String SQLWishes = """
                select
                wish.wish_name as wish_name,
                wish.description as description,
                wish.product_url as product_url,
                wish.comments as comments,
                wish.price as price,
                wish.isReserved as isReserved,
                event.event_name as event_name,
                event.event_date as event_date
                from event_wish join wish
                on wish.wishID = event_wish.wishID
                and wish.listID = ?
                join event
                on event.eventID = event_wish.eventID
                order by wish.wish_name
                """;

        return jdbcTemplate.query(SQLWishes, new WishRowMapper(), listID);
    }

    private List<Event> findEvents(String listID){
        String SQLEvents =  """
                select event.event_name as event_name,
                event.event_date as event_date
                from event
                where event.listID = ?
                """;

        RowMapper<Event> eventRowMapper = (resultSet, rowNum) -> new Event(
                resultSet.getString("event_name"), resultSet.getDate("event_date"));

        return jdbcTemplate.query(SQLEvents, eventRowMapper, listID);
    }
}
