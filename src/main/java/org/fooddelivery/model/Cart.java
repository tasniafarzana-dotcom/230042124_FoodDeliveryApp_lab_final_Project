package org.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String userId;
    private String restaurantId;
    private List<CartItem> items;
    private String couponCode;

    public Cart(String userId, String restaurantId) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>();
        this.couponCode = null;
    }

    public String getUserId() { return userId; }
    public String getRestaurantId() { return restaurantId; }
    public List<CartItem> getItems() { return items; }
    public String getCouponCode() { return couponCode; }

    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public void addItem(CartItem item) { this.items.add(item); }
    public void removeItem(CartItem item) { this.items.remove(item); }
    public void clear() { this.items.clear(); }

    public double getSubTotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
}