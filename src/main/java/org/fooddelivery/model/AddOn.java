package org.fooddelivery.model;

public class AddOn {
    private String id;
    private String name;
    private double price;
    private boolean isRequired;

    public AddOn(String id, String name, double price, boolean isRequired) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isRequired = isRequired;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isRequired() { return isRequired; }

    public void setPrice(double price) { this.price = price; }
    public void setRequired(boolean required) { isRequired = required; }
}
