package org.fooddelivery.service;

import org.fooddelivery.model.DeliveryRider;

public interface IDeliveryService {
    DeliveryRider assignRider(String orderId);
    void updateRiderLocation(String riderId, double lat, double lng);
    DeliveryRider getRiderForOrder(String orderId);
}