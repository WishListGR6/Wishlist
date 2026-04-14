package wishlistgr6.wishlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<String> getWishlist(String listID){
        return repository.getWishlist(listID);
    }
}
