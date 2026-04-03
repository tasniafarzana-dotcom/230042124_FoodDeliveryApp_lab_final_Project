package org.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DeliveryRider {
    private String id;
    private String name;
    private String phone;
    private boolean isAvailable;
    private double currentLat;
    private double currentLng;

    public DeliveryRider() {}

    public DeliveryRider(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isAvailable = true;
        this.currentLat = 0.0;
        this.currentLng = 0.0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public boolean isAvailable() { return isAvailable; }
    public double getCurrentLat() { return currentLat; }
    public double getCurrentLng() { return currentLng; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setCurrentLat(double currentLat) { this.currentLat = currentLat; }
    public void setCurrentLng(double currentLng) { this.currentLng = currentLng; }
}