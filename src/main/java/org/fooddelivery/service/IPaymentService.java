package org.fooddelivery.service;

import org.fooddelivery.model.Payment;

public interface IPaymentService {
    Payment processPayment(String orderId, String method, double amount);
    Payment getPaymentByOrder(String orderId);
}