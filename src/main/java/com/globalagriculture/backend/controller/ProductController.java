package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.dto.StockRequest;
import com.globalagriculture.backend.entity.Product;
import com.globalagriculture.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/products.js -> public browse/read endpoints
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}/stock")
    public Product updateStock(@PathVariable Long id, @RequestBody StockRequest request) {
        return productService.updateStock(id, request.getStock());
    }
}
