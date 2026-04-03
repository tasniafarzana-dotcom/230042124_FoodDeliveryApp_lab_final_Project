package org.fooddelivery.model;

public class Address {
    private String id;
    private String label;
    private String street;
    private String area;
    private String city;
    private double lat;
    private double lng;

    public Address() {}

    public Address(String id, String label, String street, String area, String city, double lat, double lng) {
        this.id = id;
        this.label = label;
        this.street = street;
        this.area = area;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getStreet() { return street; }
    public String getArea() { return area; }
    public String getCity() { return city; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }

    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
}
