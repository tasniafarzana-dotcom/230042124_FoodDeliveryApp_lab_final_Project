package org.fooddelivery.service;

import org.fooddelivery.model.AddOn;
import org.fooddelivery.model.MenuItem;

import java.util.List;

public interface IMenuService {
    MenuItem addMenuItem(String restaurantId, String name, String description, double price, String category);
    void updateAvailability(String itemId, boolean available);
    void updateQuantity(String itemId, int quantity);
    void addAddOn(String itemId, AddOn addOn);
    List<MenuItem> getAvailableItems(String restaurantId);
    List<MenuItem> getItemsByCategory(String restaurantId, String category);
}
