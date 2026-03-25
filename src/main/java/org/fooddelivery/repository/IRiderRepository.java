package org.fooddelivery.repository;

import org.fooddelivery.model.DeliveryRider;

import java.util.List;

public interface IRiderRepository extends IRepository<DeliveryRider> {
    List<DeliveryRider> findAvailableRiders();
}
