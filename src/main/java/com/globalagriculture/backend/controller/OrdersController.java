package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.dto.OrderRequest;
import com.globalagriculture.backend.entity.Orders;
import com.globalagriculture.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/orders.js -> /orders
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Orders> createOrder(@RequestBody OrderRequest request) {
        Orders order = orderService.createOrder(request.getBuyerId(), request.getShippingAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/buyer/{buyerId}")
    public List<Orders> getOrdersByBuyer(@PathVariable Long buyerId) {
        return orderService.getOrdersByBuyer(buyerId);
    }

    @GetMapping
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }
}
