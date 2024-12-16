package com.mulweb.localpoints.fragment.main;

public class LocationObject {
    private double latitude;
    private double longitude;

    public LocationObject(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getters and setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationObject{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
