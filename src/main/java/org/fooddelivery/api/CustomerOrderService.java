package org.fooddelivery.api;

import java.util.List;

import org.fooddelivery.model.Order;
import org.fooddelivery.service.IOrderService;
import org.fooddelivery.service.OrderService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class CustomerOrderService {

    private final IOrderService orderService;

    public CustomerOrderService() {
        this.orderService = new OrderService();
    }

    @WebMethod
    public Order placeOrder(
            @WebParam(name = "userId") String userId,
            @WebParam(name = "restaurantId") String restaurantId,
            @WebParam(name = "menuItemId") String menuItemId,
            @WebParam(name = "quantity") int quantity,
            @WebParam(name = "deliveryAddressId") String deliveryAddressId) {

        String addressId = (deliveryAddressId == null || deliveryAddressId.isBlank())
                ? "DEFAULT" : deliveryAddressId;

        return orderService.placeOrder(userId, restaurantId, menuItemId, quantity, addressId);
    }

    @WebMethod
    public List<Order> getOrdersByUser(@WebParam(name = "userId") String userId) {
        return orderService.getOrdersByUser(userId);
    }
}