package wishlistgr6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.service.WishlistService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishlistService wishlistService;

    private Wishlist testList;
    private MockHttpSession session;
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

        session = mock(MockHttpSession.class);
        when(session.getAttribute("wishlist")).thenReturn(testList);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void home() {
    }

    @Test
    void display_home_page() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void showLogin() {
    }

    @Test
    void login_with_valid_access_token() throws Exception{
        String listID = "abcd1234";
        String accessToken = "access12";
        List<String> accessTokens = new ArrayList<>();
        accessTokens.add(accessToken);
        when(wishlistService.getAccessTokens(listID)).thenReturn(accessTokens);
        when(wishlistService.getWishlist(listID)).thenReturn(testList);
        when(session.getAttribute("isOwner")).thenReturn(false);

        mockMvc.perform(get("/login/" + listID)
                        .param("accessToken", accessToken)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("wishlist"));

        Mockito.verify(wishlistService).getAccessTokens(listID);
    }

    @Test
    void login_without_access_token() throws Exception{
        String listID = "abcd1234";
        String accessToken = "access12";
        List<String> accessTokens = new ArrayList<>();
        accessTokens.add(accessToken);
        when(wishlistService.getAccessTokens(listID)).thenReturn(accessTokens);
        when(session.getAttribute("isOwner")).thenReturn(false);

        mockMvc.perform(get("/login/" + listID)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("listID"))
                .andExpect(model().attribute("listID", listID));

        verify(wishlistService).getAccessTokens(listID);
    }

    @Test
    void login() {
    }

    @Test
    void wishlist() {
    }

    @Test
    void newWishlist() {
    }

    @Test
    void createWishlist() {
    }
}