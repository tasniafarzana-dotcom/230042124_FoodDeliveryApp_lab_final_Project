package org.fooddelivery.api;

import org.fooddelivery.model.Order;
import org.fooddelivery.repository.IOrderRepository;
import org.fooddelivery.repository.OrderRepository;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public class OrderStatusService {

    private final IOrderRepository orderRepository;

    public OrderStatusService() {
        this.orderRepository = new OrderRepository();
    }

    @WebMethod
    public String getOrderStatus(@WebParam(name = "orderId") String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> order.getStatus().toString())
                .orElse("Order not found");
    }

    @WebMethod
    public Order getOrderDetails(@WebParam(name = "orderId") String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
