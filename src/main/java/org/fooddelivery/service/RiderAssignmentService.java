package org.fooddelivery.service;

import java.util.List;
import java.util.Optional;

import org.fooddelivery.model.RiderAssignment;
import org.fooddelivery.repository.IRiderAssignmentRepository;
import org.fooddelivery.repository.RiderAssignmentRepository;
import org.fooddelivery.util.IdGenerator;

public class RiderAssignmentService implements IRiderAssignmentService {
    
    private final IRiderAssignmentRepository assignmentRepository;

    public RiderAssignmentService() {
        this.assignmentRepository = new RiderAssignmentRepository();
    }

    @Override
    public void createAssignment(String orderId, String riderId) {
        String assignmentId = IdGenerator.generateId();
        RiderAssignment assignment = new RiderAssignment(assignmentId, orderId, riderId);
        assignmentRepository.save(assignment);
    }

    @Override
    public void respondToAssignment(String assignmentId, String response) {
        Optional<RiderAssignment> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isPresent()) {
            assignment.get().setStatus(response);  // "ACCEPTED" or "REJECTED"
            assignmentRepository.save(assignment.get());
        } else {
            throw new IllegalArgumentException("Assignment not found");
        }
    }

    @Override
    public Optional<RiderAssignment> getActiveAssignmentByOrderId(String orderId) {
        List<RiderAssignment> assignments = assignmentRepository.findByOrderId(orderId);
        return assignments.stream()
                .filter(a -> "ACCEPTED".equals(a.getStatus()))
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
}