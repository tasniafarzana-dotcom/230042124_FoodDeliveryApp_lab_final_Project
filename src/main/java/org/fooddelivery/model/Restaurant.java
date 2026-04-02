package org.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Restaurant {
    private String id;
    private String ownerId;
    private String name;
    private String cuisineType;
    private String phone;
    private Address address;
    private List<Schedule> schedules;
    private boolean isOpen;
    private double rating;
    private int totalRatings;

    public Restaurant(String id, String ownerId, String name, String cuisineType, String phone, Address address) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.phone = phone;
        this.address = address;
        this.schedules = new ArrayList<>();
        this.isOpen = false;
        this.rating = 0.0;
        this.totalRatings = 0;
    }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getCuisineType() { return cuisineType; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }
    public List<Schedule> getSchedules() { return schedules; }
    public boolean isOpen() { return isOpen; }
    public double getRating() { return rating; }
    public int getTotalRatings() { return totalRatings; }

    public void setOpen(boolean open) { isOpen = open; }
    public void setRating(double rating) { this.rating = rating; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }
    public void addSchedule(Schedule schedule) { this.schedules.add(schedule); }
}