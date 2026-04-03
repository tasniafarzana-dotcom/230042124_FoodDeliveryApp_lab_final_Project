package org.fooddelivery.service;

import java.util.List;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.repository.IMenuRepository;
import org.fooddelivery.repository.MenuRepository;
import org.fooddelivery.util.IdGenerator;
import org.fooddelivery.util.ValidationUtils;

public class MenuService implements IMenuService {

    private final IMenuRepository menuRepository;

    public MenuService() {
        this.menuRepository = new MenuRepository();
    }

    @Override
    public MenuItem addMenuItem(String restaurantId, String name, String description,
                                double price, String category) {
        if (!ValidationUtils.isNotEmpty(name))
            throw new IllegalArgumentException("Item name cannot be empty");
        if (!ValidationUtils.isPositive(price))
            throw new IllegalArgumentException("Price must be positive");

        String id = IdGenerator.generateMenuItemId();
        MenuItem item = new MenuItem(id, restaurantId, name, description, price, category);
        menuRepository.save(item);
        return item;
    }

    @Override
    public void updateAvailability(String itemId, boolean available) {
        menuRepository.findById(itemId).ifPresent(item -> {
            item.setAvailable(available);
            menuRepository.update(item);
        });
    }

    @Override
    public void updateQuantity(String itemId, int quantity) {
        if (!ValidationUtils.isPositive(quantity))
            throw new IllegalArgumentException("Quantity must be positive");
        menuRepository.findById(itemId).ifPresent(item -> {
            item.setQuantity(quantity);
            menuRepository.update(item);
        });
    }

    @Override
    public List<MenuItem> getAvailableItems(String restaurantId) {
        return menuRepository.findAvailableByRestaurantId(restaurantId);
    }

    @Override
    public List<MenuItem> getItemsByCategory(String restaurantId, String category) {
        return menuRepository.findByCategory(restaurantId, category);
    }
}