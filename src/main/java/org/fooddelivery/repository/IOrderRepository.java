package org.fooddelivery.repository;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;

import java.util.List;

public interface IOrderRepository extends IRepository<Order> {
    List<Order> findByUserId(String userId);
    List<Order> findByRestaurantId(String restaurantId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findActiveOrdersByRestaurant(String restaurantId);
}