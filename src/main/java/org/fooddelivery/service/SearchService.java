package org.fooddelivery.service;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.repository.IMenuRepository;
import org.fooddelivery.repository.IRestaurantRepository;
import org.fooddelivery.repository.MenuRepository;
import org.fooddelivery.repository.RestaurantRepository;

import java.util.List;

public class SearchService implements ISearchService {

    private final IRestaurantRepository restaurantRepository;
    private final IMenuRepository menuRepository;

    public SearchService() {
        this.restaurantRepository = new RestaurantRepository();
        this.menuRepository = new MenuRepository();
    }

    @Override
    public List<Restaurant> searchRestaurants(String query, String cuisineFilter) {
        return restaurantRepository.findAll().stream()
                .filter(r -> {
                    boolean matchesQuery = query == null || query.isEmpty()
                            || r.getName().toLowerCase().contains(query.toLowerCase())
                            || r.getAddress().getArea().toLowerCase().contains(query.toLowerCase());
                    boolean matchesCuisine = cuisineFilter == null || cuisineFilter.isEmpty()
                            || r.getCuisineType().equalsIgnoreCase(cuisineFilter);
                    return matchesQuery && matchesCuisine;
                })
                .toList();
    }

    @Override
    public List<MenuItem> searchMenuItems(String restaurantId, String query) {
        return menuRepository.findByRestaurantId(restaurantId).stream()
                .filter(item -> query == null || query.isEmpty()
                        || item.getName().toLowerCase().contains(query.toLowerCase())
                        || item.getCategory().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
}

