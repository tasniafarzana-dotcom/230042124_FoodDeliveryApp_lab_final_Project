package org.fooddelivery.service;

import org.fooddelivery.model.Cart;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.repository.IOrderRepository;
import org.fooddelivery.repository.OrderRepository;
import org.fooddelivery.util.IdGenerator;

import java.util.List;

public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ICartService cartService;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.cartService = new CartService();
    }

    @Override
    public Order placeOrder(Cart cart, String deliveryAddressId) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        double total = cartService.calculateTotal(cart);
        String id = IdGenerator.generateOrderId();
        Order order = new Order(id, cart.getUserId(), cart.getRestaurantId(),
                cart.getItems(), total, deliveryAddressId);
        orderRepository.save(order);
        cartService.clearCart(cart);
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
            if (order.getStatus() == OrderStatus.DELIVERED) {
                throw new IllegalArgumentException("Cannot cancel a delivered order");
            }
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.update(order);
        });
    }
}
