package com.globalagriculture.backend.service;

import com.globalagriculture.backend.dto.PaymentRequest;
import com.globalagriculture.backend.entity.Notification;
import com.globalagriculture.backend.entity.Orders;
import com.globalagriculture.backend.entity.Payment;
import com.globalagriculture.backend.exception.ApiException;
import com.globalagriculture.backend.repository.NotificationRepository;
import com.globalagriculture.backend.repository.OrdersRepository;
import com.globalagriculture.backend.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final NotificationRepository notificationRepository;

    public PaymentService(PaymentRepository paymentRepository,
                           OrdersRepository ordersRepository,
                           NotificationRepository notificationRepository) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Payment createPayment(PaymentRequest request) {
        Orders order = ordersRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ApiException("Order not found with id " + request.getOrderId(), HttpStatus.NOT_FOUND));

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setPaymentStatus("SUCCESS");
        payment = paymentRepository.save(payment);

        // Cash on delivery settles later; card/UPI is treated as paid immediately.
        order.setPaymentStatus("COD".equalsIgnoreCase(request.getPaymentMethod()) ? "PENDING" : "PAID");
        order.setOrderStatus("CONFIRMED");
        ordersRepository.save(order);

        Notification notification = new Notification();
        notification.setUserId(order.getBuyerId());
        notification.setTitle("Payment recorded");
        notification.setMessage("Payment for order #" + order.getOrderId() + " was recorded via " + request.getPaymentMethod() + ".");
        notification.setNotificationType("PAYMENT");
        notification.setRead(false);
        notificationRepository.save(notification);

        return payment;
    }
}
