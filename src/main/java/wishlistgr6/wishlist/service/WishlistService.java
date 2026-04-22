package wishlistgr6.wishlist.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wishlistgr6.wishlist.exceptions.DuplicateWishException;
import wishlistgr6.wishlist.exceptions.InvalidWishException;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.exceptions.EventsAlreadyExistException;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.WishlistRepository;

import java.sql.Date;
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


    public String addWish(Wish wish, String listID) {
        validateWish(wish);
        if (wish.getEvents().isEmpty()){
            wish.addEvent(new Event("No event", Date.valueOf("2050-01-01")));
        }
        try {
            return repository.addWish(wish, listID);
        } catch (DataIntegrityViolationException ex ) {
            throw new DuplicateWishException("A wish with this name already exists. Please, try another name.");
        }


    }
    public String createWishlist(Wishlist newWishlist, String ownerPassword, String guestPassword) throws EventsAlreadyExistException, SQLException {
        newWishlist.addNoEvent();
        return repository.createWishlist(newWishlist, ownerPassword, guestPassword);
    }

    public void updateWish(Wish wish, String listId, String wishName) {
        repository.updateWish(wish, listId, wishName);
    }

    public void validateWish (Wish wish) {
        if (wish == null) {
            throw new InvalidWishException("Can not create an empty wish.");
        }
        String name = wish.getName();
        if (name == null || name.isEmpty()) {
            throw new InvalidWishException("Name is required.");
        }
        if (name.length() > 100) {
        throw new InvalidWishException("Name must be less then 100 characters.");
        }
    }

    public void wishNameUnique(Wish wish, String listID) {
        // check if name of the new wish is unique in the current wishlist
        // if not unique, then throw new DuplicateWishException
    }
}
