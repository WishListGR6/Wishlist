package wishlistgr6.wishlist;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.WishlistRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository repository;

    private Wishlist testList;
    @BeforeEach
    void setUp() {

        List<Event> testEvents = new ArrayList<>();
        testEvents.add(new Event("Sample event", Date.valueOf("2026-12-24")));
        List<Wish> testWishes = new ArrayList<>();
        Wish testWish = new Wish("Sample wish", "description", "URL", "sample comments", 9.95, false);
        testWish.addEvent(testEvents.getFirst());
        testWishes.add(testWish);
        testList = new Wishlist("Sample list", testWishes, testEvents);

    }

    @Test
    void getWishlist(){
        Wishlist wishlist = repository.getWishlist("abcd1234");

        assertNotNull(wishlist);
        assertEquals(testList, wishlist);
        System.out.println(wishlist.getWishes());
    }
}
