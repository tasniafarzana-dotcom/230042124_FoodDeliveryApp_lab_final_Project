package org.fooddelivery.service;

import org.fooddelivery.model.Cart;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Cart cart, String deliveryAddressId);
    void updateStatus(String orderId, OrderStatus status);
    List<Order> getOrdersByUser(String userId);
    List<Order> getActiveOrdersByRestaurant(String restaurantId);
    void cancelOrder(String orderId);
}