package org.fooddelivery.service;

import org.fooddelivery.model.AddOn;
import org.fooddelivery.model.Cart;
import org.fooddelivery.model.MenuItem;

import java.util.List;

public interface ICartService {
    void addItem(Cart cart, MenuItem menuItem, int quantity, List<AddOn> addOns);
    void removeItem(Cart cart, String menuItemId);
    void applyCoupon(Cart cart, String couponCode);
    double calculateTotal(Cart cart);
    void clearCart(Cart cart);
}