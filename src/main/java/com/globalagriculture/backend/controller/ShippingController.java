package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.entity.Shipping;
import com.globalagriculture.backend.service.ShippingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/shipping.js -> /shipping
@RestController
@RequestMapping("/shipping")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping("/buyer/{buyerId}")
    public List<Shipping> getShippingByBuyer(@PathVariable Long buyerId) {
        return shippingService.getShippingByBuyer(buyerId);
    }

    @GetMapping
    public List<Shipping> getAllShipping() {
        return shippingService.getAllShipping();
    }
}
