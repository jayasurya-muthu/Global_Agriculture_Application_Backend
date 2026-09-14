package com.globalagriculture.backend.service;

import com.globalagriculture.backend.entity.Cart;
import com.globalagriculture.backend.entity.Product;
import com.globalagriculture.backend.exception.ApiException;
import com.globalagriculture.backend.exception.ResourceNotFoundException;
import com.globalagriculture.backend.repository.CartRepository;
import com.globalagriculture.backend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<Cart> getCartByBuyer(Long buyerId) {
        return cartRepository.findByBuyerId(buyerId);
    }

    // If the buyer already has this product in their cart, bump the quantity
    // instead of creating a duplicate row.
    public Cart addToCart(Long buyerId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        int qty = (quantity == null || quantity < 1) ? 1 : quantity;

        Cart cart = cartRepository.findByBuyerIdAndProductId(buyerId, productId)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + qty);
                    return existing;
                })
                .orElseGet(() -> new Cart(buyerId, productId, qty));

        if (cart.getQuantity() > product.getStock()) {
            throw new ApiException("Only " + product.getStock() + " " + (product.getUnit() != null ? product.getUnit() : "unit")
                    + " of " + product.getProductName() + " available", HttpStatus.BAD_REQUEST);
        }

        return cartRepository.save(cart);
    }

    public Cart updateQuantity(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id " + cartId));

        if (quantity == null || quantity < 1) {
            throw new ApiException("Quantity must be at least 1", HttpStatus.BAD_REQUEST);
        }

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (quantity > product.getStock()) {
            throw new ApiException("Only " + product.getStock() + " available", HttpStatus.BAD_REQUEST);
        }

        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    public void removeItem(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new ResourceNotFoundException("Cart item not found with id " + cartId);
        }
        cartRepository.deleteById(cartId);
    }

    public void clearBuyerCart(Long buyerId) {
        cartRepository.deleteByBuyerId(buyerId);
    }
}
