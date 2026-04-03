package org.fooddelivery.service;

import java.util.List;
import java.util.Optional;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.RiderAssignment;

public interface IRiderAssignmentService {
    void createAssignment(String orderId, String riderId);
    void respondToAssignment(String assignmentId, String response);
    Optional<RiderAssignment> getActiveAssignmentByOrderId(String orderId);
    List<RiderAssignment> getPendingTasksForRider(String riderId);
    List<RiderAssignment> getAcceptedTasksForRider(String riderId);

    
    List<Order> getAcceptedOrdersForRider(String riderId);
}