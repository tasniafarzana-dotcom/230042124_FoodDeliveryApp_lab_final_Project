package org.fooddelivery.model;

public class Coupon {
    private String code;
    private String discountType;
    private double discountValue;
    private double minOrderValue;
    private String expiryDate;
    private int usageLimit;
    private int usedCount;

    public Coupon(String code, String discountType, double discountValue, double minOrderValue, String expiryDate, int usageLimit) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.expiryDate = expiryDate;
        this.usageLimit = usageLimit;
        this.usedCount = 0;
    }

    public String getCode() { return code; }
    public String getDiscountType() { return discountType; }
    public double getDiscountValue() { return discountValue; }
    public double getMinOrderValue() { return minOrderValue; }
    public String getExpiryDate() { return expiryDate; }
    public int getUsageLimit() { return usageLimit; }
    public int getUsedCount() { return usedCount; }

    public void incrementUsedCount() { this.usedCount++; }
    public boolean isValid() { return usedCount < usageLimit; }
}
