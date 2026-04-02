package org.fooddelivery.util;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return true;
        return !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return true;
        return !phone.matches("^01[3-9][0-9]{8}$");
    }

    public static boolean isNotEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }
}
