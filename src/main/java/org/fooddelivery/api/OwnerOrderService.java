package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.DeliveryRider;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.service.DeliveryService;
import org.fooddelivery.service.IDeliveryService;
import org.fooddelivery.service.IOrderService;
import org.fooddelivery.service.OrderService;

import java.util.List;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class OwnerOrderService {
    private final IOrderService orderService;
    private final IDeliveryService deliveryService;

    public OwnerOrderService() {
        this.orderService = new OrderService();
        this.deliveryService = new DeliveryService();
    }

    @WebMethod
    public List<Order> getActiveOrdersByRestaurant(@WebParam(name = "restaurantId") String restaurantId) {
        return orderService.getActiveOrdersByRestaurant(restaurantId);
    }

    @WebMethod
    public void updateOrderStatus(
            @WebParam(name = "orderId") String orderId,
            @WebParam(name = "status") String status
    ) {
        orderService.updateStatus(orderId, OrderStatus.valueOf(status));
    }

    @WebMethod
    public DeliveryRider assignRider(@WebParam(name = "orderId") String orderId) {
        return deliveryService.assignRider(orderId);
    }
}