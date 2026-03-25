package org.fooddelivery.model;

public class Schedule {
    private String dayOfWeek;
    private String openTime;
    private String closeTime;

    public Schedule(String dayOfWeek, String openTime, String closeTime) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public String getOpenTime() { return openTime; }
    public String getCloseTime() { return closeTime; }

    public void setOpenTime(String openTime) { this.openTime = openTime; }
    public void setCloseTime(String closeTime) { this.closeTime = closeTime; }
}
