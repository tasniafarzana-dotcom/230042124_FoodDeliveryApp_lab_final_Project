package org.fooddelivery.service;

import java.util.List;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;

public interface IOrderService {
    Order placeOrder(String userId, String restaurantId, String menuItemId,
                     int quantity, String deliveryAddressId);
    void updateStatus(String orderId, OrderStatus status);
    void updateOrder(Order order);
    Order getOrderById(String orderId);
    List<Order> getOrdersByUser(String userId);
    List<Order> getActiveOrdersByRestaurant(String restaurantId);
    void cancelOrder(String orderId);
}