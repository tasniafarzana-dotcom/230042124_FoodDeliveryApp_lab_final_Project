package org.fooddelivery.service;

import org.fooddelivery.model.Coupon;
import org.fooddelivery.repository.CouponRepository;
import org.fooddelivery.repository.ICouponRepository;

public class DiscountService implements IDiscountService {

    private final ICouponRepository couponRepository;

    public DiscountService() {
        this.couponRepository = new CouponRepository();
    }

    @Override
    public Coupon validateCoupon(String code, double orderTotal) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

        if (!coupon.isValid()) {
            throw new IllegalArgumentException("Coupon usage limit exceeded");
        }
        if (orderTotal < coupon.getMinOrderValue()) {
            throw new IllegalArgumentException("Order total is less than minimum required: " + coupon.getMinOrderValue());
        }
        return coupon;
    }

    @Override
    public double applyDiscount(double orderTotal, Coupon coupon) {
        if (coupon.getDiscountType().equals("PERCENTAGE")) {
            double discount = orderTotal * (coupon.getDiscountValue() / 100);
            return orderTotal - discount;
        } else if (coupon.getDiscountType().equals("FLAT")) {
            return Math.max(0, orderTotal - coupon.getDiscountValue());
        }
        return orderTotal;
    }

    @Override
    public void markCouponUsed(String code) {
        couponRepository.findByCode(code).ifPresent(coupon -> {
            coupon.incrementUsedCount();
            couponRepository.update(coupon);
        });
    }
}