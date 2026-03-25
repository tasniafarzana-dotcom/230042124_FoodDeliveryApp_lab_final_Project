package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.MenuItem;
import org.fooddelivery.service.IMenuService;
import org.fooddelivery.service.MenuService;

import java.util.List;

@WebService
public class MenuQueryService {

    private final IMenuService menuService;

    public MenuQueryService() {
        this.menuService = new MenuService();
    }

    @WebMethod
    public List<MenuItem> getAvailableItems(@WebParam(name = "restaurantId") String restaurantId) {
        return menuService.getAvailableItems(restaurantId);
    }

    @WebMethod
    public List<MenuItem> getItemsByCategory(
            @WebParam(name = "restaurantId") String restaurantId,
            @WebParam(name = "category") String category) {
        return menuService.getItemsByCategory(restaurantId, category);
    }
}
