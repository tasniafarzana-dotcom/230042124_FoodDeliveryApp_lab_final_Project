package org.fooddelivery.repository;

import java.util.List;
import java.util.Optional;

import org.fooddelivery.model.RiderAssignment;

public interface IRiderAssignmentRepository {
    void save(RiderAssignment assignment);
    void delete(String id);
    Optional<RiderAssignment> findById(String id);
    List<RiderAssignment> findByRiderIdAndStatus(String riderId, String status);
    List<RiderAssignment> findByOrderId(String orderId);
}