package com.globalagriculture.backend.service;

import com.globalagriculture.backend.entity.*;
import com.globalagriculture.backend.exception.ApiException;
import com.globalagriculture.backend.exception.ResourceNotFoundException;
import com.globalagriculture.backend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ShippingRepository shippingRepository;
    private final NotificationRepository notificationRepository;

    public OrderService(OrdersRepository ordersRepository,
                         OrderItemRepository orderItemRepository,
                         CartRepository cartRepository,
                         ProductRepository productRepository,
                         ShippingRepository shippingRepository,
                         NotificationRepository notificationRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.shippingRepository = shippingRepository;
        this.notificationRepository = notificationRepository;
    }

    // Builds an order + order_items snapshot from the buyer's current cart,
    // reduces stock, and opens a PENDING shipment record. The cart itself is
    // left alone here — the frontend clears it after payment succeeds.
    @Transactional
    public Orders createOrder(Long buyerId, String shippingAddress) {
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new ApiException("Shipping address is required", HttpStatus.BAD_REQUEST);
        }

        List<Cart> cartItems = cartRepository.findByBuyerId(buyerId);
        if (cartItems.isEmpty()) {
            throw new ApiException("Your cart is empty", HttpStatus.BAD_REQUEST);
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Cart item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + item.getProductId()));
            if (item.getQuantity() > product.getStock()) {
                throw new ApiException("Not enough stock for " + product.getProductName(), HttpStatus.BAD_REQUEST);
            }
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Orders order = new Orders();
        order.setBuyerId(buyerId);
        order.setShippingAddress(shippingAddress);
        order.setTotalAmount(total);
        order.setOrderStatus("PLACED");
        order.setPaymentStatus("PENDING");
        order = ordersRepository.save(order);

        for (Cart item : cartItems) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            orderItemRepository.save(new OrderItem(order.getOrderId(), product.getProductId(), item.getQuantity(), subtotal));

            product.setStock(product.getStock() - item.getQuantity());
            product.syncStatus();
            productRepository.save(product);
        }

        Shipping shipping = new Shipping();
        shipping.setOrderId(order.getOrderId());
        shipping.setBuyerId(buyerId);
        shipping.setShippingStatus("PENDING");
        shippingRepository.save(shipping);

        Notification notification = new Notification();
        notification.setUserId(buyerId);
        notification.setTitle("Order placed");
        notification.setMessage("Your order #" + order.getOrderId() + " has been placed successfully.");
        notification.setNotificationType("ORDER");
        notification.setRead(false);
        notificationRepository.save(notification);

        return order;
    }

    public List<Orders> getOrdersByBuyer(Long buyerId) {
        return ordersRepository.findByBuyerId(buyerId);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public List<OrderItem> getItemsByOrder(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Orders getOrderById(Long orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }

    public Orders save(Orders order) {
        return ordersRepository.save(order);
    }
}
