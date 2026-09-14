package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.dto.CartRequest;
import com.globalagriculture.backend.dto.QuantityRequest;
import com.globalagriculture.backend.entity.Cart;
import com.globalagriculture.backend.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/cart.js -> /cart
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/buyer/{buyerId}")
    public List<Cart> getCartByBuyer(@PathVariable Long buyerId) {
        return cartService.getCartByBuyer(buyerId);
    }

    @PostMapping
    public ResponseEntity<Cart> addToCart(@RequestBody CartRequest request) {
        Cart cart = cartService.addToCart(request.getBuyerId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @PutMapping("/{cartId}")
    public Cart updateQuantity(@PathVariable Long cartId, @RequestBody QuantityRequest request) {
        return cartService.updateQuantity(cartId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartId) {
        cartService.removeItem(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/buyer/{buyerId}")
    public ResponseEntity<Void> clearBuyerCart(@PathVariable Long buyerId) {
        cartService.clearBuyerCart(buyerId);
        return ResponseEntity.noContent().build();
    }
}
