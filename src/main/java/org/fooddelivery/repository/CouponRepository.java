package org.fooddelivery.repository;

import com.google.gson.reflect.TypeToken;
import org.fooddelivery.model.Coupon;

import java.util.List;
import java.util.Optional;

public class CouponRepository extends FileRepository<Coupon> implements ICouponRepository {

    public CouponRepository() {
        super("data/coupons.json", new TypeToken<List<Coupon>>(){}.getType());
    }

    @Override
    protected String getId(Coupon coupon) {
        return coupon.getCode();
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return findAll().stream()
                .filter(coupon -> coupon.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    @Override
    public List<Coupon> findValidCoupons() {
        return findAll().stream()
                .filter(Coupon::isValid)
                .toList();
    }
}