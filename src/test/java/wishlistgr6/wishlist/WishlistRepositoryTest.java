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
import java.sql.SQLException;
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

    private String createNewWishlist(Wishlist newTestlist){
        newTestlist.addNoEvent();
        newTestlist.setName("Testlist");
        String testOwnerPassword = "ownerPassword";
        String testGuestPassword = "guestPassword";
        String listID;
        try {
            listID = repository.createWishlist(newTestlist, testOwnerPassword, testGuestPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listID;
    }
    @Test
    void createWishlist(){
        Wishlist newTestlist = new Wishlist();
        String listID = createNewWishlist(newTestlist);
        assertEquals(newTestlist, repository.getWishlist(listID));
        assertNotEquals(repository.getWishlist("abcd1234"), repository.getWishlist(listID));
    }
    @Test
    void createWishlistContainsNoEvent(){
        Wishlist newTestlist = new Wishlist();
        String listID = createNewWishlist(newTestlist);
        assert(repository.getWishlist(listID).getEvents().contains(testList.getEvents().getFirst()));
        assert(newTestlist.getEvents().contains(testList.getEvents().getFirst()));
    }
    @Test
    void createWishlistTestPasswords(){
        Wishlist newTestlist = new Wishlist();
        String testOwnerPassword = "ownerPassword";
        String testGuestPassword = "guestPassword";
        String listID = createNewWishlist(newTestlist);
        assertTrue(repository.checkOwnerPassword(listID, testOwnerPassword));
        assertTrue(repository.checkGuestPassword(listID, testGuestPassword));
        assertFalse(repository.checkOwnerPassword(listID, "Wrong Password"));
        assertFalse(repository.checkGuestPassword(listID, "Wrong Password"));
    }

    @Test
    void addWishWithNoEvent() {
        Wish newWish = new Wish("New Wish", "description", "URL", "sample comments", 9.95, false);
        newWish.addEvent(testList.getEvents().getFirst());
        repository.addWish(newWish, "abcd1234");
        Wishlist foundWishList = repository.getWishlist("abcd1234");
        Wish foundWish = foundWishList.getWishes().getLast();
        assertEquals(newWish, foundWish);
    }
    @Test
    void addWishWithOneEvent() {
        Wish newWish2 = new Wish("New Wish 2", "description 2", "URL 2", "sample comments 2", 14.95, false);
        newWish2.addEvent(testList.getEvents().get(1));
        repository.addWish(newWish2, "abcd1234");
        Wishlist foundWishList = repository.getWishlist("abcd1234");
        Wish foundWish = foundWishList.getWishes().getLast();
        assertEquals(newWish2, foundWish);
    }
    @Test
    void addWishTwoEvents() {
        Wish newWish3 = new Wish("New Wish 3", "description 3", "URL 3", "sample comments 3", 99.95, false);
        newWish3.addEvent(testList.getEvents().get(1));
        newWish3.addEvent(testList.getEvents().get(2));
        repository.addWish(newWish3, "abcd1234");
        Wishlist foundWishList = repository.getWishlist("abcd1234");
        Wish foundWish = foundWishList.getWishes().getLast();
        assertEquals(newWish3, foundWish);
    }

}
