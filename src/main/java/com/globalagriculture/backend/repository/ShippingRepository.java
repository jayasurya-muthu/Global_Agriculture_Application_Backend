package com.globalagriculture.backend.repository;

import com.globalagriculture.backend.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    List<Shipping> findByBuyerId(Long buyerId);
    List<Shipping> findByOrderId(Long orderId);
}
