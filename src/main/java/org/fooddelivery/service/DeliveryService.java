package org.fooddelivery.service;

import org.fooddelivery.model.DeliveryRider;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.repository.IOrderRepository;
import org.fooddelivery.repository.IRiderRepository;
import org.fooddelivery.repository.OrderRepository;
import org.fooddelivery.repository.RiderRepository;

public class DeliveryService implements IDeliveryService {

    private final IRiderRepository riderRepository;
    private final IOrderRepository orderRepository;

    public DeliveryService() {
        this.riderRepository = new RiderRepository();
        this.orderRepository = new OrderRepository();
    }

    @Override
    public DeliveryRider assignRider(String orderId) {
        DeliveryRider rider = riderRepository.findAvailableRiders()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No available riders at the moment"));

        rider.setAvailable(false);
        riderRepository.update(rider);

        orderRepository.findById(orderId).ifPresent(order -> {
            order.setRiderId(rider.getId());
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            orderRepository.update(order);
        });

        return rider;
    }

    @Override
    public void updateRiderLocation(String riderId, double lat, double lng) {
        riderRepository.findById(riderId).ifPresent(rider -> {
            rider.setCurrentLat(lat);
            rider.setCurrentLng(lng);
            riderRepository.update(rider);
        });
    }

    @Override
    public DeliveryRider getRiderForOrder(String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> riderRepository.findById(order.getRiderId())
                        .orElseThrow(() -> new IllegalArgumentException("Rider not found")))
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}