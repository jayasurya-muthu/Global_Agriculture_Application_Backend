package com.globalagriculture.backend.service;

import com.globalagriculture.backend.entity.Wishlist;
import com.globalagriculture.backend.exception.ApiException;
import com.globalagriculture.backend.repository.WishlistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<Wishlist> getWishlist(Long buyerId) {
        return wishlistRepository.findByBuyerId(buyerId);
    }

    public Wishlist addToWishlist(Long buyerId, Long productId) {
        return wishlistRepository.findByBuyerIdAndProductId(buyerId, productId)
                .orElseGet(() -> wishlistRepository.save(new Wishlist(buyerId, productId)));
    }

    public void removeFromWishlist(Long buyerId, Long productId) {
        wishlistRepository.findByBuyerIdAndProductId(buyerId, productId)
                .orElseThrow(() -> new ApiException("Item not found in wishlist", HttpStatus.NOT_FOUND));
        wishlistRepository.deleteByBuyerIdAndProductId(buyerId, productId);
    }
}
