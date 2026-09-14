package com.globalagriculture.backend.service;

import com.globalagriculture.backend.entity.Shipping;
import com.globalagriculture.backend.repository.ShippingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    public List<Shipping> getShippingByBuyer(Long buyerId) {
        return shippingRepository.findByBuyerId(buyerId);
    }

    public List<Shipping> getAllShipping() {
        return shippingRepository.findAll();
    }
}
