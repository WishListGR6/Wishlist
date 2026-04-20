package wishlistgr6.wishlist.controller.exceptions;

public class WishNotFoundException extends RuntimeException {
    public WishNotFoundException() {
        super("No wish with that name");
    }
}
