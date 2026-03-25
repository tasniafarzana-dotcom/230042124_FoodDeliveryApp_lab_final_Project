package org.fooddelivery.repository;

import com.google.gson.reflect.TypeToken;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;

import java.util.List;

public class OrderRepository extends FileRepository<Order> implements IOrderRepository {

    public OrderRepository() {
        super("data/orders.json", new TypeToken<List<Order>>(){}.getType());
    }

    @Override
    protected String getId(Order order) {
        return order.getId();
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return findAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Order> findByRestaurantId(String restaurantId) {
        return findAll().stream()
                .filter(order -> order.getRestaurantId().equals(restaurantId))
                .toList();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return findAll().stream()
                .filter(order -> order.getStatus() == status)
                .toList();
    }

    @Override
    public List<Order> findActiveOrdersByRestaurant(String restaurantId) {
        return findAll().stream()
                .filter(order -> order.getRestaurantId().equals(restaurantId))
                .filter(order -> order.getStatus() != OrderStatus.DELIVERED
                        && order.getStatus() != OrderStatus.CANCELLED)
                .toList();
    }
}