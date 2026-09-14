package com.globalagriculture.backend.service;

import com.globalagriculture.backend.entity.Product;
import com.globalagriculture.backend.exception.ResourceNotFoundException;
import com.globalagriculture.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public Product createProduct(Product product) {
        product.syncStatus();
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updated) {
        Product existing = getProductById(id);
        existing.setProductName(updated.getProductName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        existing.setUnit(updated.getUnit());
        existing.setImageUrl(updated.getImageUrl());
        existing.setCountryOfOrigin(updated.getCountryOfOrigin());
        existing.setCategoryId(updated.getCategoryId());
        existing.setHarvestDate(updated.getHarvestDate());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.syncStatus();
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        Product existing = getProductById(id);
        productRepository.delete(existing);
    }

    public Product updateStock(Long id, Integer stock) {
        Product existing = getProductById(id);
        existing.setStock(stock);
        existing.syncStatus();
        return productRepository.save(existing);
    }
}
