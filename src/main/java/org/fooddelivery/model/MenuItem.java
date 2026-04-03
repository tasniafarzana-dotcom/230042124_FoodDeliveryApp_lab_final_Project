package org.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MenuItem {
    private String id;
    private String restaurantId;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean isAvailable;
    private int quantity;

    public MenuItem() {}

    public MenuItem(String id, String restaurantId, String name, String description,
                    double price, String category) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = true;
        this.quantity = 100;
    }

    public String getId() { return id; }
    public String getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public int getQuantity() { return quantity; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
}