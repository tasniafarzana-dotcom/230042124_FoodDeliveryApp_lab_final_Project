package org.fooddelivery.service;

import java.util.List;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderItem;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.repository.IMenuRepository;
import org.fooddelivery.repository.IOrderRepository;
import org.fooddelivery.repository.MenuRepository;
import org.fooddelivery.repository.OrderRepository;
import org.fooddelivery.util.IdGenerator;

public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IMenuRepository menuRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.menuRepository = new MenuRepository();
    }

    @Override
    public Order placeOrder(String userId, String restaurantId, String menuItemId,
                            int quantity, String deliveryAddressId) {

        MenuItem item = menuRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + menuItemId));

        if (!item.isAvailable())
            throw new IllegalArgumentException("Item is not available");
        if (item.getQuantity() < quantity)
            throw new IllegalArgumentException("Not enough stock");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        OrderItem orderItem = new OrderItem(item, quantity);
        double total = orderItem.getTotalPrice();

        String id = IdGenerator.generateOrderId();
        Order order = new Order(id, userId, restaurantId, List.of(orderItem), total, deliveryAddressId);
        orderRepository.save(order);

        // Stock কমানো
        item.setQuantity(item.getQuantity() - quantity);
        menuRepository.update(item);

        return order;
    }

    @Override
    public void updateStatus(String orderId, OrderStatus status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.update(order);
        });
    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.update(order);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getActiveOrdersByRestaurant(String restaurantId) {
        return orderRepository.findActiveOrdersByRestaurant(restaurantId);
    }

    @Override
    public void cancelOrder(String orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            if (order.getStatus() == OrderStatus.DELIVERED)
                throw new IllegalArgumentException("Cannot cancel a delivered order");
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.update(order);
        });
    }
}