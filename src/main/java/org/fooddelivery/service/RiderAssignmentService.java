package org.fooddelivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.RiderAssignment;
import org.fooddelivery.repository.IOrderRepository;
import org.fooddelivery.repository.IRiderAssignmentRepository;
import org.fooddelivery.repository.IRiderRepository;
import org.fooddelivery.repository.OrderRepository;
import org.fooddelivery.repository.RiderAssignmentRepository;
import org.fooddelivery.repository.RiderRepository;
import org.fooddelivery.util.IdGenerator;

public class RiderAssignmentService implements IRiderAssignmentService {

    private final IRiderAssignmentRepository assignmentRepository;
    private final IOrderRepository orderRepository;
    private final IRiderRepository riderRepository;

    public RiderAssignmentService() {
        this.assignmentRepository = new RiderAssignmentRepository();
        this.orderRepository = new OrderRepository();
        this.riderRepository = new RiderRepository();
    }

    @Override
    public void createAssignment(String orderId, String riderId) {
        List<RiderAssignment> assignments = assignmentRepository.findByOrderId(orderId);

        boolean hasOpenAssignment = assignments.stream()
                .anyMatch(a -> "PENDING".equalsIgnoreCase(a.getStatus())
                        || "ACCEPTED".equalsIgnoreCase(a.getStatus()));

        if (hasOpenAssignment) {
            throw new IllegalArgumentException("Order already has a pending or accepted rider assignment");
        }

        String assignmentId = IdGenerator.generateId();
        RiderAssignment assignment = new RiderAssignment(assignmentId, orderId, riderId);
        assignmentRepository.save(assignment);
    }

    @Override
    public void respondToAssignment(String assignmentId, String response) {
        Optional<RiderAssignment> assignmentOpt = assignmentRepository.findById(assignmentId);
        if (assignmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Assignment not found");
        }

        RiderAssignment assignment = assignmentOpt.get();
        String normalized = response == null ? "" : response.trim().toUpperCase();

        if (!"ACCEPTED".equals(normalized) && !"REJECTED".equals(normalized)) {
            throw new IllegalArgumentException("Response must be ACCEPTED or REJECTED");
        }

        assignment.setStatus(normalized);
        assignmentRepository.save(assignment);

        // If rejected, rider becomes available again
        if ("REJECTED".equals(normalized)) {
            riderRepository.findById(assignment.getRiderId()).ifPresent(rider -> {
                rider.setAvailable(true);
                riderRepository.update(rider);
            });
        }
    }

    @Override
    public Optional<RiderAssignment> getActiveAssignmentByOrderId(String orderId) {
        List<RiderAssignment> assignments = assignmentRepository.findByOrderId(orderId);
        return assignments.stream()
                .filter(a -> "ACCEPTED".equalsIgnoreCase(a.getStatus()))
                .findFirst();
    }

    @Override
    public List<RiderAssignment> getPendingTasksForRider(String riderId) {
        return assignmentRepository.findByRiderIdAndStatus(riderId, "PENDING");
    }

    @Override
    public List<RiderAssignment> getAcceptedTasksForRider(String riderId) {
        return assignmentRepository.findByRiderIdAndStatus(riderId, "ACCEPTED");
    }

    @Override
    public List<Order> getAcceptedOrdersForRider(String riderId) {
        List<RiderAssignment> assignments = getAcceptedTasksForRider(riderId);
        List<Order> orders = new ArrayList<>();

        for (RiderAssignment assignment : assignments) {
            orderRepository.findById(assignment.getOrderId()).ifPresent(order -> {
                order.setAssignmentId(assignment.getId());
                order.setRiderId(assignment.getRiderId());
                orders.add(order);
            });
        }

        return orders;
    }
}