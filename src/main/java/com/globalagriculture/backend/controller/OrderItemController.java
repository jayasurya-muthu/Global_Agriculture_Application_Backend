package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.entity.OrderItem;
import com.globalagriculture.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/orders.js -> /order-items
@RestController
@RequestMapping("/order-items")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class OrderItemController {

    private final OrderService orderService;

    public OrderItemController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order/{orderId}")
    public List<OrderItem> getItemsByOrder(@PathVariable Long orderId) {
        return orderService.getItemsByOrder(orderId);
    }
}
