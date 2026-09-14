package com.globalagriculture.backend.repository;

import com.globalagriculture.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
