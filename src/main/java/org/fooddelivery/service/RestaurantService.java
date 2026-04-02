package org.fooddelivery.service;

import org.fooddelivery.model.Address;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.Schedule;
import org.fooddelivery.repository.IRestaurantRepository;
import org.fooddelivery.repository.RestaurantRepository;
import org.fooddelivery.util.GeoUtils;
import org.fooddelivery.util.IdGenerator;
import org.fooddelivery.util.ValidationUtils;

import java.util.Comparator;
import java.util.List;

public class RestaurantService implements IRestaurantService {

    private final IRestaurantRepository restaurantRepository;

    public RestaurantService() {
        this.restaurantRepository = new RestaurantRepository();
    }

    @Override
    public Restaurant registerRestaurant(String ownerId, String name, String cuisineType, String phone, Address address) {
        if (ValidationUtils.isNotEmpty(name)) {
            throw new IllegalArgumentException("Restaurant name cannot be empty");
        }
        if (ValidationUtils.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        String id = IdGenerator.generateRestaurantId();
        Restaurant restaurant = new Restaurant(id, ownerId, name, cuisineType, phone, address);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public List<Restaurant> findNearestRestaurants(double userLat, double userLng, double radiusKm) {
        return restaurantRepository.findOpenRestaurants().stream()
                .filter(r -> GeoUtils.haversineDistance(
                        userLat, userLng,
                        r.getAddress().getLat(),
                        r.getAddress().getLng()) <= radiusKm)
                .sorted(Comparator.comparingDouble(r -> GeoUtils.haversineDistance(
                        userLat, userLng,
                        r.getAddress().getLat(),
                        r.getAddress().getLng())))
                .toList();
    }

    @Override
    public List<Restaurant> findByArea(String area) {
        if (ValidationUtils.isNotEmpty(area)) {
            throw new IllegalArgumentException("Area cannot be empty");
        }
        return restaurantRepository.findByArea(area);
    }

    @Override
    public List<Restaurant> getOwnerRestaurants(String ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    @Override
    public void setOpenStatus(String restaurantId, boolean isOpen) {
        restaurantRepository.findById(restaurantId).ifPresent(r -> {
            r.setOpen(isOpen);
            restaurantRepository.update(r);
        });
    }

    @Override
    public void addSchedule(String restaurantId, Schedule schedule) {
        restaurantRepository.findById(restaurantId).ifPresent(r -> {
            r.addSchedule(schedule);
            restaurantRepository.update(r);
        });
    }

    @Override
    public void updateRating(String restaurantId, double newRating) {
        restaurantRepository.findById(restaurantId).ifPresent(r -> {
            int total = r.getTotalRatings() + 1;
            double updated = ((r.getRating() * r.getTotalRatings()) + newRating) / total;
            r.setRating(updated);
            r.setTotalRatings(total);
            restaurantRepository.update(r);
        });
    }
}
