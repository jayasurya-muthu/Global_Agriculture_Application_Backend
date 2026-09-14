package com.globalagriculture.backend.config;

import com.globalagriculture.backend.entity.*;
import com.globalagriculture.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Seeds the database on first run so the app is usable immediately:
 * - an ADMIN account (admin@globalagriculture.com / admin123)
 * - produce categories
 * - a handful of sample products
 * Only runs when the relevant tables are empty, so it's safe to leave in place.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                       CategoryRepository categoryRepository,
                       ProductRepository productRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        List<Category> categories = seedCategories();
        seedProducts(categories);
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@globalagriculture.com")) return;

        User admin = new User();
        admin.setFullName("Global Agriculture Admin");
        admin.setEmail("admin@globalagriculture.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhone("9999999999");
        admin.setAddress("Head Office");
        admin.setCity("Bengaluru");
        admin.setState("Karnataka");
        admin.setCountry("India");
        admin.setPincode("560001");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);
    }

    private List<Category> seedCategories() {
        if (categoryRepository.count() > 0) return categoryRepository.findAll();

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Vegetables", "Fresh farm vegetables"));
        categories.add(new Category("Fruits", "Seasonal fruits"));
        categories.add(new Category("Grains", "Wheat, rice, and cereals"));
        categories.add(new Category("Spices", "Herbs and spices"));
        categories.add(new Category("Pulses", "Lentils and legumes"));
        categories.add(new Category("Dairy", "Milk and dairy products"));
        categories.add(new Category("Herbs", "Fresh herbs"));
        return categoryRepository.saveAll(categories);
    }

    private void seedProducts(List<Category> categories) {
        if (productRepository.count() > 0 || categories.isEmpty()) return;

        Long vegetables = categoryId(categories, "Vegetables");
        Long fruits = categoryId(categories, "Fruits");
        Long grains = categoryId(categories, "Grains");

        Product zucchini = new Product();
        zucchini.setProductName("Zucchini");
        zucchini.setDescription("Fresh green zucchini, hand-picked.");
        zucchini.setPrice(new BigDecimal("45.00"));
        zucchini.setStock(120);
        zucchini.setUnit("kg");
        zucchini.setImageUrl("/images/products/Zucchini.jpg");
        zucchini.setCountryOfOrigin("India");
        zucchini.setCategoryId(vegetables);
        zucchini.setHarvestDate(LocalDate.now().minusDays(1));
        zucchini.setExpiryDate(LocalDate.now().plusDays(6));
        zucchini.setStatus("Available");

        Product wheat = new Product();
        wheat.setProductName("Whole Wheat");
        wheat.setDescription("Stone-ground whole wheat, farm sourced.");
        wheat.setPrice(new BigDecimal("38.00"));
        wheat.setStock(300);
        wheat.setUnit("kg");
        wheat.setImageUrl("/images/products/Whole Wheat.jpg");
        wheat.setCountryOfOrigin("India");
        wheat.setCategoryId(grains);
        wheat.setHarvestDate(LocalDate.now().minusDays(10));
        wheat.setExpiryDate(LocalDate.now().plusDays(180));
        wheat.setStatus("Available");

        Product mango = new Product();
        mango.setProductName("Alphonso Mango");
        mango.setDescription("Sweet, ripe Alphonso mangoes.");
        mango.setPrice(new BigDecimal("120.00"));
        mango.setStock(0);
        mango.setUnit("dozen");
        mango.setImageUrl("");
        mango.setCountryOfOrigin("India");
        mango.setCategoryId(fruits);
        mango.setHarvestDate(LocalDate.now().minusDays(3));
        mango.setExpiryDate(LocalDate.now().plusDays(4));
        mango.setStatus("Out_of_Stock");

        productRepository.saveAll(List.of(zucchini, wheat, mango));
    }

    private Long categoryId(List<Category> categories, String name) {
        return categories.stream()
                .filter(c -> c.getCategoryName().equals(name))
                .map(Category::getCategoryId)
                .findFirst()
                .orElse(categories.get(0).getCategoryId());
    }
}
