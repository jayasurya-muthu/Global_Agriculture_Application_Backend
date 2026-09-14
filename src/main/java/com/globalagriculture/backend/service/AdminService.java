package com.globalagriculture.backend.service;

import com.globalagriculture.backend.dto.DashboardStatistics;
import com.globalagriculture.backend.entity.Orders;
import com.globalagriculture.backend.entity.User;
import com.globalagriculture.backend.repository.OrdersRepository;
import com.globalagriculture.backend.repository.ProductRepository;
import com.globalagriculture.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;

    public AdminService(UserRepository userRepository, ProductRepository productRepository, OrdersRepository ordersRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.ordersRepository = ordersRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public DashboardStatistics getStatistics() {
        List<Orders> orders = ordersRepository.findAll();
        BigDecimal revenue = orders.stream()
                .map(Orders::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardStatistics(
                userRepository.count(),
                productRepository.count(),
                orders.size(),
                revenue
        );
    }
}
