package org.fooddelivery.api;

import java.util.List;

import org.fooddelivery.model.DeliveryRider;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.repository.RiderRepository;
import org.fooddelivery.service.IOrderService;
import org.fooddelivery.service.IRiderAssignmentService;
import org.fooddelivery.service.OrderService;
import org.fooddelivery.service.RiderAssignmentService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class OwnerOrderService {

    private final IOrderService orderService;
    private final IRiderAssignmentService assignmentService;
    private final RiderRepository riderRepository;

    public OwnerOrderService() {
        this.orderService = new OrderService();
        this.assignmentService = new RiderAssignmentService();
        this.riderRepository = new RiderRepository();
    }

    @WebMethod
    public List<Order> getActiveOrdersByRestaurant(
            @WebParam(name = "restaurantId") String restaurantId) {
        return orderService.getActiveOrdersByRestaurant(restaurantId);
    }

    @WebMethod
    public void updateOrderStatus(
            @WebParam(name = "orderId") String orderId,
            @WebParam(name = "status") String status) {
        orderService.updateStatus(orderId, OrderStatus.valueOf(status));
    }

    @WebMethod
    public List<DeliveryRider> getAvailableRiders() {
        return riderRepository.findAvailableRiders();
    }

    
    @WebMethod
    public String assignRider(@WebParam(name = "orderId") String orderId) {
        List<DeliveryRider> available = riderRepository.findAvailableRiders();

        if (available.isEmpty()) {
            throw new IllegalArgumentException("No available riders at the moment");
        }

        DeliveryRider rider = available.get(0);

        assignmentService.createAssignment(orderId, rider.getId());
        rider.setAvailable(false);
        riderRepository.update(rider);

        return "Rider " + rider.getName() + " (" + rider.getId() + ") assigned successfully!";
    }

    @WebMethod
    public String assignRiderToOrder(
            @WebParam(name = "orderId") String orderId,
            @WebParam(name = "riderId") String riderId) {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (riderId == null || riderId.isBlank()) {
            throw new IllegalArgumentException("Rider ID is required");
        }

        DeliveryRider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new IllegalArgumentException("Rider not found: " + riderId));

        if (!rider.isAvailable()) {
            throw new IllegalArgumentException("Selected rider is not available");
        }

        assignmentService.createAssignment(orderId, rider.getId());
        rider.setAvailable(false);
        riderRepository.update(rider);

        return "Rider " + rider.getName() + " (" + rider.getId() + ") assigned successfully!";
    }
}