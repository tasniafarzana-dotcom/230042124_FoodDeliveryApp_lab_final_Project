package org.fooddelivery.model;

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
    
    

    public Restaurant() {}

    public Restaurant(String id, String ownerId, String name, String cuisineType,
                      String phone, Address address) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.phone = phone;
        this.address = address;
        
    }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getCuisineType() { return cuisineType; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }
   
}