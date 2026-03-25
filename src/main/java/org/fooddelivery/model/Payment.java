package org.fooddelivery.model;

public class Payment {
    private String id;
    private String orderId;
    private String method;
    private double amount;
    private String status;
    private String transactionId;
    private String paidAt;

    public Payment(String id, String orderId, String method, double amount) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.status = "PENDING";
        this.transactionId = null;
        this.paidAt = null;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
    public String getPaidAt() { return paidAt; }

    public void setStatus(String status) { this.status = status; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setPaidAt(String paidAt) { this.paidAt = paidAt; }
}