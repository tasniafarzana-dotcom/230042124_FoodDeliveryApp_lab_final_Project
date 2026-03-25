package org.fooddelivery.model;

import java.util.List;

public class Order {
    private String id;
    private String userId;
    private String restaurantId;
    private String riderId;
    private List<CartItem> items;
    private double totalPrice;
    private OrderStatus status;
    private String placedAt;
    private String deliveryAddressId;

    public Order(String id, String userId, String restaurantId, List<CartItem> items, double totalPrice, String deliveryAddressId) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.deliveryAddressId = deliveryAddressId;
        this.status = OrderStatus.PLACED;
        this.riderId = null;
        this.placedAt = java.time.LocalDateTime.now().toString();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getRestaurantId() { return restaurantId; }
    public String getRiderId() { return riderId; }
    public List<CartItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public OrderStatus getStatus() { return status; }
    public String getPlacedAt() { return placedAt; }
    public String getDeliveryAddressId() { return deliveryAddressId; }

    public void setStatus(OrderStatus status) { this.status = status; }
    public void setRiderId(String riderId) { this.riderId = riderId; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
