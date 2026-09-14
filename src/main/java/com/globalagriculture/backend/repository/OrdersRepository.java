package com.globalagriculture.backend.repository;

import com.globalagriculture.backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByBuyerId(Long buyerId);
}
