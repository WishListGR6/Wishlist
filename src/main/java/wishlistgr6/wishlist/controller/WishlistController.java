package wishlistgr6.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wishlistgr6.wishlist.service.WishlistService;

@Controller
@RequestMapping("/")
public class WishlistController {
    private final WishlistService service;
    public WishlistController(WishlistService service) {this.service = service;}

}
