package org.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private String role;
    private List<String> addressIds;

    public User() {}

    public User(String id, String name, String email, String phone,
                String passwordHash, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
        this.addressIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public List<String> getAddressIds() { return addressIds; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void addAddressId(String addressId) { this.addressIds.add(addressId); }
}