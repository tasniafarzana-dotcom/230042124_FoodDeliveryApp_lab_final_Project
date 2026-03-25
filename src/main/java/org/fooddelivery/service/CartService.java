package org.fooddelivery.service;

import org.fooddelivery.model.AddOn;
import org.fooddelivery.model.Cart;
import org.fooddelivery.model.CartItem;
import org.fooddelivery.model.Coupon;
import org.fooddelivery.model.MenuItem;

import java.util.List;

public class CartService implements ICartService {

    private final IDiscountService discountService;

    public CartService() {
        this.discountService = new DiscountService();
    }

    @Override
    public void addItem(Cart cart, MenuItem menuItem, int quantity, List<AddOn> addOns) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!menuItem.isAvailable()) {
            throw new IllegalArgumentException("Item is not available");
        }
        if (menuItem.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }

        CartItem cartItem = new CartItem(menuItem, quantity);
        if (addOns != null) {
            addOns.forEach(cartItem::addSelectedAddOn);
        }
        cart.addItem(cartItem);
    }

    @Override
    public void removeItem(Cart cart, String menuItemId) {
        cart.getItems().removeIf(item -> item.getMenuItem().getId().equals(menuItemId));
    }

    @Override
    public void applyCoupon(Cart cart, String couponCode) {
        double subTotal = cart.getSubTotal();
        discountService.validateCoupon(couponCode, subTotal);
        cart.setCouponCode(couponCode);
    }

    @Override
    public double calculateTotal(Cart cart) {
        double subTotal = cart.getSubTotal();
        if (cart.getCouponCode() != null) {
            Coupon coupon = discountService.validateCoupon(cart.getCouponCode(), subTotal);
            return discountService.applyDiscount(subTotal, coupon);
        }
        return subTotal;
    }

    @Override
    public void clearCart(Cart cart) {
        cart.clear();
        cart.setCouponCode(null);
    }
}
