package wishlistgr6.wishlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wishlistgr6.wishlist.exceptions.EventsAlreadyExistException;
import wishlistgr6.wishlist.exceptions.WishNotFoundException;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.WishlistRepository;

import java.sql.SQLException;
import java.util.List;

@Transactional
@Service
public class WishlistService {
    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {this.repository = repository;}

    public List<String> getAccessTokens(String listID){
        return repository.getAccessTokens(listID);
    }

    public Wishlist getWishlist(String listID){
        return repository.getWishlist(listID);
    }

    public String shareLink(String listID) {
        return repository.generateShareLink(listID);
    }

    public boolean checkOwnerPassword(String listID, String password){return repository.checkOwnerPassword(listID, password);}
    public boolean checkGuestPassword(String listID, String password){return repository.checkGuestPassword(listID, password);}

    public String createWishlist(Wishlist newWishlist, String ownerPassword, String guestPassword) throws EventsAlreadyExistException, SQLException {
        newWishlist.addNoEvent();
        return repository.createWishlist(newWishlist, ownerPassword, guestPassword);
    }

    public void updateWish(Wish wish, String listId, String wishName) {
        repository.updateWish(wish, listId, wishName);
    }

    public void deleteWish(Wish wish){
        try {
            repository.deleteWish(wish);
        } catch (WrongThreadException e) {
            throw new WishNotFoundException();
        }
    }
}
