package org.fooddelivery.repository;

import com.google.gson.reflect.TypeToken;
import org.fooddelivery.model.MenuItem;

import java.util.List;

public class MenuRepository extends FileRepository<MenuItem> implements IMenuRepository {

    public MenuRepository() {
        super("data/menu.json", new TypeToken<List<MenuItem>>(){}.getType());
    }

    @Override
    protected String getId(MenuItem menuItem) {
        return menuItem.getId();
    }

    @Override
    public List<MenuItem> findByRestaurantId(String restaurantId) {
        return findAll().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
                .toList();
    }

    @Override
    public List<MenuItem> findAvailableByRestaurantId(String restaurantId) {
        return findAll().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
                .filter(item -> item.isAvailable() && item.getQuantity() > 0)
                .toList();
    }

    @Override
    public List<MenuItem> findByCategory(String restaurantId, String category) {
        return findAll().stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId))
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}