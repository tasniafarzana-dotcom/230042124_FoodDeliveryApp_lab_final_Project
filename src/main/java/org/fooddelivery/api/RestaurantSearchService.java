package org.fooddelivery.api;

import java.util.List;

import org.fooddelivery.model.Address;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.service.IRestaurantService;
import org.fooddelivery.service.RestaurantService;
import org.fooddelivery.util.IdGenerator;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public class RestaurantSearchService {

    private final IRestaurantService restaurantService;

    public RestaurantSearchService() {
        this.restaurantService = new RestaurantService();
    }

    @WebMethod
    public List<Restaurant> getRestaurantsByArea(@WebParam(name = "area") String area) {
        return restaurantService.findByArea(area);
    }

    @WebMethod
    public List<Restaurant> getNearestRestaurants(
            @WebParam(name = "lat") double lat,
            @WebParam(name = "lng") double lng,
            @WebParam(name = "radiusKm") double radiusKm) {
        return restaurantService.findNearestRestaurants(lat, lng, radiusKm);
    }

    
    @WebMethod
    public Restaurant registerRestaurant(
            @WebParam(name = "ownerId") String ownerId,
            @WebParam(name = "n") String name,
            @WebParam(name = "cuisineType") String cuisineType,
            @WebParam(name = "phone") String phone,
            @WebParam(name = "street") String street,
            @WebParam(name = "area") String area,
            @WebParam(name = "city") String city,
            @WebParam(name = "latitude") double latitude,
            @WebParam(name = "longitude") double longitude) {

        Address address = new Address(
            IdGenerator.generateAddressId(),
            "Main", street, area, city, latitude, longitude
        );
        return restaurantService.registerRestaurant(
            ownerId, name, cuisineType, phone, address
        );
    }

   
    @WebMethod
    public List<Restaurant> getOwnerRestaurants(
            @WebParam(name = "ownerId") String ownerId) {
        return restaurantService.getOwnerRestaurants(ownerId);
    }
}