package org.fooddelivery.service;

import java.time.LocalDateTime;

import org.fooddelivery.model.Payment;
import org.fooddelivery.repository.IPaymentRepository;
import org.fooddelivery.repository.PaymentRepository;
import org.fooddelivery.util.IdGenerator;

public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;

    public PaymentService() {
        this.paymentRepository = new PaymentRepository();
    }

    @Override
    public Payment processPayment(String orderId, String method, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (!method.equals("CASH") && !method.equals("CARD") && !method.equals("MOBILE_BANKING"))
            throw new IllegalArgumentException("Invalid payment method");

        String id = IdGenerator.generatePaymentId();
        Payment payment = new Payment(id, orderId, method, amount);

        payment.setTransactionId("TXN-" + IdGenerator.generatePaymentId());
        payment.setStatus("COMPLETED");
        payment.setPaidAt(LocalDateTime.now().toString());

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPaymentByOrder(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found for order: " + orderId));
    }
}