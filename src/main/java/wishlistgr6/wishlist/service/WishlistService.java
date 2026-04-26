package wishlistgr6.wishlist.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wishlistgr6.wishlist.exceptions.EventsAlreadyExistException;
import wishlistgr6.wishlist.exceptions.WishNotFoundException;
import wishlistgr6.wishlist.exceptions.DuplicateWishException;
import wishlistgr6.wishlist.exceptions.InvalidWishException;
import wishlistgr6.wishlist.model.Event;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.WishlistRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

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


    public void addWish(Wish wish, String listID) {
        validateWish(wish);
        if (wish.getEvents().isEmpty()){
            wish.addEvent(new Event("No event", Date.valueOf("2050-01-01")));
        }
        try {
            repository.addWish(wish, listID);
        } catch (DataIntegrityViolationException ex ) {
            throw new DuplicateWishException("Der findes allerede et ønske med det navn.");
        }

    }
    public String createWishlist(Wishlist newWishlist, String ownerPassword, String guestPassword) throws EventsAlreadyExistException, SQLException {
        newWishlist.addNoEvent();
        return repository.createWishlist(newWishlist, ownerPassword, guestPassword);
    }

    public void reserveWish(String listID, String wishName) {
        Wishlist wishlist = repository.getWishlist(listID);
        Wish wish = wishlist.findWishInWishList(wishName);
        wish.setReserved(true);
        updateWish(wish, listID, wishName);
    }



    public void updateWish(Wish wish, String listId, String wishName) {
        if (wish.getEvents().isEmpty()) {
            wish.addEvent(new Event("No event", Date.valueOf("2050-01-01")));
        }
        try {
            repository.updateWish(wish, listId, wishName);
        } catch (NoSuchElementException e) {
            throw new WishNotFoundException("Cant find a wish with that name");
        }
    }

    public void deleteWish(Wish wish){
        try {
            repository.deleteWish(wish);
        } catch (WishNotFoundException e) {
            throw new WishNotFoundException("No wish with that name exists.");
        }
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

}
