package org.fooddelivery.repository;

import java.util.List;
import java.util.Optional;

import org.fooddelivery.model.Payment;

import com.google.gson.reflect.TypeToken;

public class PaymentRepository extends FileRepository<Payment> implements IPaymentRepository {

    public PaymentRepository() {
        super("data/payments.json", new TypeToken<List<Payment>>(){}.getType());
    }

    @Override
    protected String getId(Payment payment) {
        return payment.getId();
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        return findAll().stream()
                .filter(payment -> payment.getOrderId().equals(orderId))
                .findFirst();
    }

    @Override
    public List<Payment> findByStatus(String status) {
        return findAll().stream()
                .filter(payment -> payment.getStatus().equalsIgnoreCase(status))
                .toList();
    }
}