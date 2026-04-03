package org.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RiderAssignment {
    private String id;
    private String orderId;
    private String riderId;
    private String status;
    private String assignedAt;
    private String respondedAt;

    public RiderAssignment() {}

    public RiderAssignment(String id, String orderId, String riderId) {
        this.id = id;
        this.orderId = orderId;
        this.riderId = riderId;
        this.status = "PENDING";
        this.assignedAt = java.time.LocalDateTime.now().toString();
        this.respondedAt = null;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getRiderId() { return riderId; }
    public String getStatus() { return status; }
    public String getAssignedAt() { return assignedAt; }
    public String getRespondedAt() { return respondedAt; }

    public void setId(String id) { this.id = id; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }
    public void setStatus(String status) {
        this.status = status;
        this.respondedAt = java.time.LocalDateTime.now().toString();
    }
    public void setAssignedAt(String assignedAt) { this.assignedAt = assignedAt; }
    public void setRespondedAt(String respondedAt) { this.respondedAt = respondedAt; }
}