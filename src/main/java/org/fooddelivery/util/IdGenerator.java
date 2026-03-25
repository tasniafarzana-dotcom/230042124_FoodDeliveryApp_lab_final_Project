package org.fooddelivery.util;

import java.util.UUID;

public class IdGenerator {

    public static String generateUserId() {
        return "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateRestaurantId() {
        return "RST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateMenuItemId() {
        return "ITM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generatePaymentId() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateRiderId() {
        return "RDR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateAddressId() {
        return "ADR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateCouponId() {
        return "CPN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}