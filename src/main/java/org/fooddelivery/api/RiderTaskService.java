package org.fooddelivery.api;

import java.util.List;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.model.RiderAssignment;
import org.fooddelivery.repository.RiderRepository;
import org.fooddelivery.service.IOrderService;
import org.fooddelivery.service.IRiderAssignmentService;
import org.fooddelivery.service.OrderService;
import org.fooddelivery.service.RiderAssignmentService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class RiderTaskService {

    private final IRiderAssignmentService assignmentService;
    private final RiderRepository riderRepository;
    private final IOrderService orderService;

    public RiderTaskService() {
        this.assignmentService = new RiderAssignmentService();
        this.riderRepository = new RiderRepository();
        this.orderService = new OrderService();
    }

    @WebMethod
    public List<RiderAssignment> getPendingTasksForRider(
            @WebParam(name = "riderId") String riderId) {
        return assignmentService.getPendingTasksForRider(riderId);
    }

    @WebMethod
    public List<RiderAssignment> getAcceptedTasksForRider(
            @WebParam(name = "riderId") String riderId) {
        return assignmentService.getAcceptedTasksForRider(riderId);
    }

    @WebMethod
    public List<Order> getAcceptedOrdersForRider(
            @WebParam(name = "riderId") String riderId) {
        return assignmentService.getAcceptedOrdersForRider(riderId);
    }

    @WebMethod
    public void respondToAssignment(
            @WebParam(name = "assignmentId") String assignmentId,
            @WebParam(name = "response") String response) {
        assignmentService.respondToAssignment(assignmentId, response);
    }

    @WebMethod
    public String completeDelivery(
            @WebParam(name = "orderId") String orderId,
            @WebParam(name = "riderId") String riderId) {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (riderId == null || riderId.isBlank()) {
            throw new IllegalArgumentException("Rider ID is required");
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }

        orderService.updateStatus(orderId, OrderStatus.DELIVERED);

        riderRepository.findById(riderId).ifPresentOrElse(rider -> {
            rider.setAvailable(true);
            riderRepository.update(rider);
        }, () -> {
            throw new IllegalArgumentException("Rider not found: " + riderId);
        });

        return "Order " + orderId + " marked as delivered and rider " + riderId + " is available again.";
    }

    @WebMethod
    public void markRiderAvailable(
            @WebParam(name = "riderId") String riderId) {
        riderRepository.findById(riderId).ifPresent(rider -> {
            rider.setAvailable(true);
            riderRepository.update(rider);
        });
    }
}