package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.DeliveryRider;
import org.fooddelivery.service.DeliveryService;
import org.fooddelivery.service.IDeliveryService;

@WebService
public class DeliveryTrackingService {

    private final IDeliveryService deliveryService;

    public DeliveryTrackingService() {
        this.deliveryService = new DeliveryService();
    }

    @WebMethod
    public DeliveryRider getRiderForOrder(@WebParam(name = "orderId") String orderId) {
        return deliveryService.getRiderForOrder(orderId);
    }

    @WebMethod
    public String getRiderLocation(@WebParam(name = "orderId") String orderId) {
        DeliveryRider rider = deliveryService.getRiderForOrder(orderId);
        return "Lat: " + rider.getCurrentLat() + ", Lng: " + rider.getCurrentLng();
    }
}
