package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.entity.Category;
import com.globalagriculture.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/categories.js -> GET /categories
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
