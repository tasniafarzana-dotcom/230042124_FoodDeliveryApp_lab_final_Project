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
    public String assignRider(@WebParam(name = "orderId") String orderId) {
        List<DeliveryRider> available = riderRepository.findAvailableRiders();
        if (available.isEmpty())
            throw new IllegalArgumentException("No available riders at the moment");
        DeliveryRider rider = available.get(0);
        assignmentService.createAssignment(orderId, rider.getId());
        return "Rider " + rider.getName() + " assigned! Waiting for acceptance.";
    }
}