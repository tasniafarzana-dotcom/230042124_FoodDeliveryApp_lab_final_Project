package org.fooddelivery.repository;

import org.fooddelivery.model.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository extends IRepository<Payment> {
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByStatus(String status);
}
