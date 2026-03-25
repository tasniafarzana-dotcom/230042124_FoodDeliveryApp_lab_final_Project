package org.fooddelivery.api;

import jakarta.xml.ws.Endpoint;

public class ApiServer {

    private static final String BASE_URL = "http://localhost:8080/";

    public static void startAll() {
        Endpoint.publish(BASE_URL + "restaurants", new RestaurantSearchService());
        Endpoint.publish(BASE_URL + "menu", new MenuQueryService());
        Endpoint.publish(BASE_URL + "order/status", new OrderStatusService());
        Endpoint.publish(BASE_URL + "delivery", new DeliveryTrackingService());

        System.out.println("SOAP Services started:");
        System.out.println("  " + BASE_URL + "restaurants?wsdl");
        System.out.println("  " + BASE_URL + "menu?wsdl");
        System.out.println("  " + BASE_URL + "order/status?wsdl");
        System.out.println("  " + BASE_URL + "delivery?wsdl");
    }
}
