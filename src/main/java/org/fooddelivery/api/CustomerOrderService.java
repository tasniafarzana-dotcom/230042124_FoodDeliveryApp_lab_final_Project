package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.Cart;
import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Order;
import org.fooddelivery.repository.IMenuRepository;
import org.fooddelivery.repository.MenuRepository;
import org.fooddelivery.service.CartService;
import org.fooddelivery.service.ICartService;
import org.fooddelivery.service.IOrderService;
import org.fooddelivery.service.OrderService;

import java.util.List;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class CustomerOrderService {
    private final IMenuRepository menuRepository;
    private final ICartService cartService;
    private final IOrderService orderService;

    public CustomerOrderService() {
        this.menuRepository = new MenuRepository();
        this.cartService = new CartService();
        this.orderService = new OrderService();
    }

    @WebMethod
    public Order placeOrder(
            @WebParam(name = "userId") String userId,
            @WebParam(name = "restaurantId") String restaurantId,
            @WebParam(name = "menuItemId") String menuItemId,
            @WebParam(name = "quantity") int quantity,
            @WebParam(name = "couponCode") String couponCode,
            @WebParam(name = "deliveryAddressId") String deliveryAddressId
    ) {
        MenuItem item = menuRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + menuItemId));

        Cart cart = new Cart(userId, restaurantId);
        cartService.addItem(cart, item, quantity, null);

        if (couponCode != null && !couponCode.isBlank()) {
            cartService.applyCoupon(cart, couponCode);
        }

        String addressId = (deliveryAddressId == null || deliveryAddressId.isBlank())
                ? "DEFAULT"
                : deliveryAddressId;

        return orderService.placeOrder(cart, addressId);
    }

    @WebMethod
    public List<Order> getOrdersByUser(@WebParam(name = "userId") String userId) {
        return orderService.getOrdersByUser(userId);
    }
}