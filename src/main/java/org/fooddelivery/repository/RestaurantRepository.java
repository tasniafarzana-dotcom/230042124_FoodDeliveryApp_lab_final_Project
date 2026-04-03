package org.fooddelivery.repository;

import java.util.List;

import org.fooddelivery.model.Restaurant;

import com.google.gson.reflect.TypeToken;

public class RestaurantRepository extends FileRepository<Restaurant> implements IRestaurantRepository {

    public RestaurantRepository() {
        super("data/restaurants.json", new TypeToken<List<Restaurant>>(){}.getType());
    }

    @Override
    protected String getId(Restaurant restaurant) {
        return restaurant.getId();
    }

    @Override
    public List<Restaurant> findByArea(String area) {
        return findAll().stream()
                .filter(r -> r.getAddress().getArea().equalsIgnoreCase(area))
                .toList();
    }

    @Override
    public List<Restaurant> findByOwnerId(String ownerId) {
        return findAll().stream()
                .filter(r -> r.getOwnerId().equals(ownerId))
                .toList();
    }

    @Override
    public List<Restaurant> findByCuisine(String cuisineType) {
        return findAll().stream()
                .filter(r -> r.getCuisineType().equalsIgnoreCase(cuisineType))
                .toList();
    }

    @Override
    public List<Restaurant> findOpenRestaurants() {
        return findAll().stream()
                .toList();
    }
}
