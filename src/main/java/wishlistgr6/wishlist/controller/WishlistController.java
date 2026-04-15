package wishlistgr6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.service.WishlistService;

import java.util.Random;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class WishlistController {
    private final WishlistService service;
    private Wishlist wishlist;
    private boolean isOwner;
    public WishlistController(WishlistService service) {this.service = service;}

    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("isOwner") !=null;
    }

    @GetMapping("/login/{listID}")
    public String showLogin(@PathVariable String listID, @RequestParam String accessToken, Model model, HttpSession session){
        //check if acessToken is good
        //pre-authenticated login
        if(service.getAccessTokens(listID).contains(accessToken) && accessToken != null){
            session.setAttribute("wishlist", service.getWishlist(listID));
            session.setAttribute("isOwner", false);
            return "wishlist";
        }

        model.addAttribute("listID", listID);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute String password, Model model, HttpSession session) {
        if (service.checkOwnerPassword(model.getAttribute("listID"), password)){
            session.setAttribute("isOwner", true);
            session.setAttribute("wishlist", service.getWishlist((String) model.getAttribute("listID")));
            return "redirect:/wishlist";
        }

        if (service.checkGuestPassword(model.getAttribute("listID"), password)){
            session.setAttribute("isOwner", false);
            session.setAttribute("wishlist", service.getWishlist((String) model.getAttribute("listID")));
            return "redirect:/wishlist";
        }

        model.addAttribute("invalidPassword", true);
        return "login/" + model.getAttribute("listID");
    }

    @GetMapping("/createWish")
    public String createWish(Model model) {
        Wish wish = new Wish();
        model.addAttribute("wish", wish);
        return "new-wish";
    }

    @PostMapping("/addWish")
    public String addWish(@ModelAttribute Wish wish) {
        service.addWish(wish);
        return "redirect:/wishlist";
    }
}
