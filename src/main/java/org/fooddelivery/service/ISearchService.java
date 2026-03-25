package org.fooddelivery.service;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;

import java.util.List;

public interface ISearchService {
    List<Restaurant> searchRestaurants(String query, String cuisineFilter);
    List<MenuItem> searchMenuItems(String restaurantId, String query);
}
