package org.fooddelivery.service;

import org.fooddelivery.model.Payment;

public interface IPaymentService {
    Payment processPayment(String orderId, String method, double amount);
    void refund(String paymentId);
    Payment getPaymentByOrder(String orderId);
}