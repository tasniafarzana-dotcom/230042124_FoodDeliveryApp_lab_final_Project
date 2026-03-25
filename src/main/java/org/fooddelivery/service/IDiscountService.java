package org.fooddelivery.service;

import org.fooddelivery.model.Coupon;

public interface IDiscountService {
    Coupon validateCoupon(String code, double orderTotal);
    double applyDiscount(double orderTotal, Coupon coupon);
    void markCouponUsed(String code);
}
