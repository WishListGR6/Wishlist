package wishlistgr6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wishlistgr6.wishlist.controller.exceptions.WishNotFoundException;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.WishlistRepository;
import wishlistgr6.wishlist.service.WishlistService;

@Controller
@RequestMapping("/")
public class WishlistController {
    private final WishlistService service;
    private final WishlistRepository wishlistRepository;
    private Wishlist wishlist;
    private boolean isOwner;
    public WishlistController(WishlistService service, WishlistRepository wishlistRepository) {this.service = service;
        this.wishlistRepository = wishlistRepository;
    }

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
            session.setAttribute("listID", listID);
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
            return "redirect:/wishlist";
        }

        if (service.checkGuestPassword(listID, password)){
            session.setAttribute("isOwner", false);
            session.setAttribute("wishlist", service.getWishlist(listID));
            session.setAttribute("listID", listID);
            return "redirect:/wishlist";
        }

        model.addAttribute("invalidPassword", true);
        return "login";
    }

    @GetMapping("/wishlist")
    public String wishlist() {
        return "wishlist";
    }

    @GetMapping("/createWish")
    public String createWish(Model model, HttpSession session) {
        Wish wish = new Wish();

        model.addAttribute("wish", wish);
        return "new-wish";
    }

    @PostMapping("/addWish")
    public String addWish(@ModelAttribute Wish wish, String listID, HttpSession session) {
        service.addWish(wish, listID);
        return "redirect:/wishlist";
    }


    @GetMapping("/wishlist/edit/{wishName}")
    public String editWish(@PathVariable String wishName, HttpSession session, Model model) {
        Wishlist wishlist = (Wishlist) session.getAttribute("wishlist");
        Wish wish = wishlist.getWishes().stream()
                .filter(w -> w.getName().equals(wishName))
                .findFirst().orElseThrow(() -> new WishNotFoundException());
        model.addAttribute("wish", wish);
        return "edit-wishlist";
    }

//
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
        Wishlist wishlist = (Wishlist) session.getAttribute("wishlist");
        Wish wish = wishlist.getWishes().stream()
                .filter(w -> w.getName().equals(wishName))
                .findFirst().orElseThrow(() -> new WishNotFoundException());
        wish.setReserved(true);
        service.updateWish(wish, listID, wishName);
        session.setAttribute("wishlist", service.getWishlist(listID));
        return "redirect:/wishlist";
    }


}
