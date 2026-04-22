package wishlistgr6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.service.WishlistService;

import java.sql.SQLException;

@Controller
@RequestMapping("/")
public class WishlistController {
    private final WishlistService service;
    private Wishlist wishlist;
    private boolean isOwner;
    private String id;
    public WishlistController(WishlistService service) {this.service = service;}

    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("listID") !=null;
    }
    private void logout(HttpSession session){
        wishlist = null;
        isOwner = false;
        id = null;
        session.invalidate();
    }


    @GetMapping("/")
    public String home(){
        return "login";
    }

    @GetMapping("/login/{listID}")
    public String showLogin(@PathVariable String listID, @RequestParam(required = false) String accessToken, Model model, HttpSession session){
        //check if acessToken is good
        //pre-authenticated login
        if(service.getAccessTokens(listID).contains(accessToken) && accessToken != null){
            session.setAttribute("wishlist", service.getWishlist(listID));
            session.setAttribute("isOwner", false);
            session.setAttribute("listID", listID);
            this.id=listID;


            return "wishlist";
        }

        model.addAttribute("listID", listID);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String listID, @RequestParam String password, Model model, HttpSession session) {
        if (service.checkOwnerPassword(listID, password)){
            session.setAttribute("isOwner", true);
            session.setAttribute("wishlist", service.getWishlist(listID));
            session.setAttribute("listID", listID);
            this.id=listID;

            return "redirect:/wishlist";
        }

        if (service.checkGuestPassword(listID, password)){
            session.setAttribute("isOwner", false);
            session.setAttribute("wishlist", service.getWishlist(listID));
            session.setAttribute("listID", listID);
            this.id=listID;

            return "redirect:/wishlist";
        }

        model.addAttribute("invalidPassword", true);
        return "login";
    }

    @GetMapping("/wishlist")
    public String wishlist() {
        return "wishlist";
    }

    @GetMapping("/wishlist/create")
    public String newWishlist(Model model, HttpSession session){
        if (isLoggedIn(session)){
            logout(session);
        }
        Wishlist newWishlist = new Wishlist();
        model.addAttribute("newWishlist", newWishlist);
        model.addAttribute("ownerPassword", "");
        model.addAttribute("guestPassword", "");
        return "create-wishlist";
    }

    @PostMapping("/wishlist/create")
    public String createWishlist(@ModelAttribute Wishlist newWishlist,
                                 @RequestParam String ownerPassword,
                                 @RequestParam String guestPassword, HttpSession session){
        this.wishlist = newWishlist;
        String newListID;
        try {
            newListID = service.createWishlist(newWishlist, ownerPassword, guestPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session.setAttribute("wishlist", newWishlist);
        session.setAttribute("isOwner", true);
        session.setAttribute("listID", newListID);
        this.id=newListID;

        return "redirect:/wishlist";
    }

    @GetMapping("/wishlist/share")
    public String shareWishList(HttpSession session) {
        String id = (String) session.getAttribute("listID");
        session.setAttribute("shareURL", service.shareLink(id));
        return "share-wishlist";
    }




    @GetMapping("/wishlist/edit/{wishName}")
    public String editWish(@PathVariable String wishName, HttpSession session, Model model) {
        String listID = (String) session.getAttribute("listID");
        Wishlist wishlist = (Wishlist) session.getAttribute("wishlist");
        Wish wish = wishlist.findWishInWishList(wishName);
        service.updateWish(wish, listID, wishName);
        model.addAttribute("wish", wish);
        return "edit-wishlist";
    }

    @PostMapping("/wishlist/edit/{wishName}")
    public String updateWish(@PathVariable String wishName, @ModelAttribute Wish wish, HttpSession session) {
        String listID = (String) session.getAttribute("listID");
        service.updateWish(wish, listID, wishName);
        session.setAttribute("wishlist", service.getWishlist(listID));
        return "redirect:/wishlist";
    }

    @PostMapping("/wishlist/reserve/{wishName}")
    public String reserveWish(@PathVariable String wishName, HttpSession session) {
        String listID = (String) session.getAttribute("listID");
        service.reserveWish(listID, wishName);
        session.setAttribute("wishlist", service.getWishlist(listID));
        return "redirect:/wishlist";
    }



}
