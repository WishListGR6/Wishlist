package wishlistgr6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.service.WishlistService;

import java.io.Serializable;

@Controller
@RequestMapping("/")
public class WishlistController implements Serializable {
    private final WishlistService service;
    private Wishlist wishlist;
    private boolean isOwner;
    public WishlistController(WishlistService service) {this.service = service;}

    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("isOwner") !=null;
    }


    @GetMapping("/")
    public String home(){
        return "login";
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
    public String login(@RequestParam String listID, @RequestParam String password, Model model, HttpSession session) {
        if (service.checkOwnerPassword(listID, password)){
            session.setAttribute("isOwner", true);
            session.setAttribute("wishlist", service.getWishlist(listID));
            return "redirect:/wishlist";
        }

        if (service.checkGuestPassword(listID, password)){
            session.setAttribute("isOwner", false);
            session.setAttribute("wishlist", service.getWishlist(listID));
            return "redirect:/wishlist";
        }

        model.addAttribute("invalidPassword", true);
        return "login";
    }

    @GetMapping("/wishlist")
    public String wishlist(Model model, HttpSession session) {
        wishlist = (Wishlist) session.getAttribute("wishlist");
        model.addAttribute("wishlist", wishlist);
        return "wishlist";
    }

}
