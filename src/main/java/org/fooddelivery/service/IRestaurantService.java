package org.fooddelivery.service;

import org.fooddelivery.model.Address;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.Schedule;

import java.util.List;

public interface IRestaurantService {
    Restaurant registerRestaurant(String ownerId, String name, String cuisineType, String phone, Address address);
    List<Restaurant> findNearestRestaurants(double userLat, double userLng, double radiusKm);
    List<Restaurant> findByArea(String area);
    List<Restaurant> getOwnerRestaurants(String ownerId);
    void setOpenStatus(String restaurantId, boolean isOpen);
    void addSchedule(String restaurantId, Schedule schedule);
    void updateRating(String restaurantId, double newRating);
}
