package org.fooddelivery.service;

import java.util.List;

import org.fooddelivery.model.MenuItem;

public interface IMenuService {
    MenuItem addMenuItem(String restaurantId, String name, String description,
                         double price, String category, int quantity);

    void updateAvailability(String itemId, boolean available);
    void updateQuantity(String itemId, int quantity);
    List<MenuItem> getAvailableItems(String restaurantId);
    List<MenuItem> getItemsByCategory(String restaurantId, String category);
}