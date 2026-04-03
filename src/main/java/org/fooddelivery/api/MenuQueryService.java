package org.fooddelivery.api;

import java.util.List;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.service.IMenuService;
import org.fooddelivery.service.MenuService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class MenuQueryService {

    private final IMenuService menuService;

    public MenuQueryService() {
        this.menuService = new MenuService();
    }

    @WebMethod
    public List<MenuItem> getAvailableItems(
            @WebParam(name = "restaurantId") String restaurantId) {
        return menuService.getAvailableItems(restaurantId);
    }

    @WebMethod
    public List<MenuItem> getItemsByCategory(
            @WebParam(name = "restaurantId") String restaurantId,
            @WebParam(name = "category") String category) {
        return menuService.getItemsByCategory(restaurantId, category);
    }

    @WebMethod
    public MenuItem addMenuItem(
            @WebParam(name = "restaurantId") String restaurantId,
            @WebParam(name = "n") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") double price,
            @WebParam(name = "category") String category,
            @WebParam(name = "quantity") int quantity) {
        return menuService.addMenuItem(restaurantId, name, description, price, category, quantity);
    }
}