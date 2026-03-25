package org.fooddelivery.repository;

import org.fooddelivery.model.Restaurant;

import java.util.List;

public interface IRestaurantRepository extends IRepository<Restaurant> {
    List<Restaurant> findByArea(String area);
    List<Restaurant> findByOwnerId(String ownerId);
    List<Restaurant> findByCuisine(String cuisineType);
    List<Restaurant> findOpenRestaurants();
}