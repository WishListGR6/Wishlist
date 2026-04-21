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
import java.util.NoSuchElementException;

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
        testEvents.add((new Event("No event", Date.valueOf("2050-01-01"))));
        testEvents.add(new Event("Sample event", Date.valueOf("2026-12-24")));
        testEvents.add(new Event("Sample event 2", Date.valueOf("2027-05-13")));
        List<Wish> testWishes = new ArrayList<>();
        Wish testWish = new Wish("Sample wish", "description", "URL", "sample comments", 9.95, false);
        Wish testWish2 = new Wish("Sample wish 2", "description 2", "URL 2", "sample comments 2", 14.95, false);
        Wish testWish3 = new Wish("Sample wish 3", "description 3", "URL 3", "sample comments 3", 99.95, false);
        testWish.addEvent(testEvents.get(1));
        testWish2.addEvent(testEvents.getFirst());
        testWish3.addEvent(testEvents.get(1));
        testWish3.addEvent(testEvents.getLast());
        testWishes.add(testWish);
        testWishes.add(testWish2);
        testWishes.add(testWish3);
        testList = new Wishlist("Sample list", testWishes, testEvents);

    }


    @Test
    void reserveWishInWishlist() {
        String wishName = "Sample wish";
        Wish reservedWish = new Wish(wishName, "description", "URL", "sample comments", 9.95, true);
        repository.updateWish(reservedWish, "abcd1234", wishName);
        Wishlist wishlist = repository.getWishlist("abcd1234");
        Wish result = wishlist.getWishes().stream()
                .filter(w -> w.getName().equals(wishName))
                .findFirst()
                .orElseThrow();
        assertTrue(result.isReserved());
    }



    @Test
    void updateWishinWishList() {
        String originalWishName = "Sample wish";
        Wish updatedWish = new Wish(originalWishName, "new description", "new URL", "new sample comments", 8.95, false);
        repository.updateWish(updatedWish, "abcd1234", originalWishName);
        Wishlist wishlist = repository.getWishlist("abcd1234");
        Wish result = wishlist.getWishes().stream()
                .filter(w -> w.getName().equals(originalWishName))
                .findFirst()
                .orElseThrow();
        assertEquals("new description", result.getDescription());
        assertEquals("new URL", result.getProductURL());
        assertEquals("new sample comments", result.getComments());
        assertEquals(8.95, result.getPrice());
    }

    @Test
    void updateWishNameInWishList() {
        String originalWishName = "Sample wish";
        Wish updatedWish = new Wish("Renamed wish", "description", "URL", "sample comments", 9.95, false);

        repository.updateWish(updatedWish, "abcd1234", originalWishName);

        Wishlist wishlist = repository.getWishlist("abcd1234");
        assertTrue(wishlist.getWishes().stream().anyMatch(w -> w.getName().equals("Renamed wish")));
        assertFalse(wishlist.getWishes().stream().anyMatch(w -> w.getName().equals(originalWishName)));
    }

    @Test
    void getWishlist(){
        Wishlist wishlist = repository.getWishlist("abcd1234");

        assertNotNull(wishlist);
        assertEquals(testList, wishlist);
    }

    @Test
    void getWishlistBadListID(){
        assertThrows(NoSuchElementException.class, () -> {repository.getWishlist("errorList");});
    }

    @Test
    void getAccessTokenValidListIDAndToken(){
        assert(repository.getAccessTokens("abcd1234").contains("access12"));
    }
    @Test
    void getAccessTokenValidListIDWrongToken(){
        assert(!repository.getAccessTokens("abcd1234").contains("wrongToken"));
    }
    @Test
    void getAccessTokenInvalidListID(){
        assert(repository.getAccessTokens("errorList").isEmpty());
    }
    @Test
    void correctOwnerPassword(){
        assertTrue(repository.checkOwnerPassword("abcd1234", "o1234"));
    }
    @Test
    void inCorrectOwnerPassword(){
        assertFalse(repository.checkOwnerPassword("abcd1234", "Wrong password"));
    }
    @Test
    void correctGuestPassword(){
        assertTrue(repository.checkGuestPassword("abcd1234", "g1234"));
    }
    @Test
    void inCorrectGuestPassword(){
        assertFalse(repository.checkGuestPassword("abcd1234", "Wrong password"));
    }

}
