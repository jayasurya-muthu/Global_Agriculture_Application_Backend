package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.dto.WishlistRequest;
import com.globalagriculture.backend.entity.Wishlist;
import com.globalagriculture.backend.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/wishlist.js -> /api/wishlist
@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/buyer/{buyerId}")
    public List<Wishlist> getWishlist(@PathVariable Long buyerId) {
        return wishlistService.getWishlist(buyerId);
    }

    @PostMapping
    public ResponseEntity<Wishlist> addToWishlist(@RequestBody WishlistRequest request) {
        Wishlist wishlist = wishlistService.addToWishlist(request.getBuyerId(), request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
    }

    @DeleteMapping("/buyer/{buyerId}/product/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long buyerId, @PathVariable Long productId) {
        wishlistService.removeFromWishlist(buyerId, productId);
        return ResponseEntity.noContent().build();
    }
}
