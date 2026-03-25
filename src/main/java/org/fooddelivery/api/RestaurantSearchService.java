package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.service.IRestaurantService;
import org.fooddelivery.service.RestaurantService;

import java.util.List;

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
}
