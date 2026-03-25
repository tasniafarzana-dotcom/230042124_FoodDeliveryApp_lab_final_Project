package org.fooddelivery.repository;

import org.fooddelivery.model.Coupon;

import java.util.List;
import java.util.Optional;

public interface ICouponRepository extends IRepository<Coupon> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findValidCoupons();
}
