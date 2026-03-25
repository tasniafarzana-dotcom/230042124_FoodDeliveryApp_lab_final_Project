package org.fooddelivery.repository;

import org.fooddelivery.model.MenuItem;

import java.util.List;

public interface IMenuRepository extends IRepository<MenuItem> {
    List<MenuItem> findByRestaurantId(String restaurantId);
    List<MenuItem> findAvailableByRestaurantId(String restaurantId);
    List<MenuItem> findByCategory(String restaurantId, String category);
}
