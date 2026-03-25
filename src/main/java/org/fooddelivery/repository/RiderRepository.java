package org.fooddelivery.repository;

import com.google.gson.reflect.TypeToken;
import org.fooddelivery.model.DeliveryRider;

import java.util.List;

public class RiderRepository extends FileRepository<DeliveryRider> implements IRiderRepository {

    public RiderRepository() {
        super("data/riders.json", new TypeToken<List<DeliveryRider>>(){}.getType());
    }

    @Override
    protected String getId(DeliveryRider rider) {
        return rider.getId();
    }

    @Override
    public List<DeliveryRider> findAvailableRiders() {
        return findAll().stream()
                .filter(DeliveryRider::isAvailable)
                .toList();
    }
}
