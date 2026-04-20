package wishlistgr6.wishlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wishlistgr6.wishlist.model.Wish;
import wishlistgr6.wishlist.model.Wishlist;
import wishlistgr6.wishlist.repository.WishlistRepository;

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

    public boolean checkOwnerPassword(String listID, String password){return repository.checkOwnerPassword(listID, password);}
    public boolean checkGuestPassword(String listID, String password){return repository.checkGuestPassword(listID, password);}


    public String addWish(Wish wish, String listID) {
        return repository.addWish(wish, listID);
    }

    public void updateWish(Wish wish, String listId, String wishName) {
        repository.updateWish(wish, listId, wishName);
    }
}
