package com.example.saguaro.bean;

public class Localisation {

    private double longitude;
    private double latitude;

    public Localisation(double longitude, double lattitude) {
        this.longitude = longitude;
        this.latitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return latitude;
    }

    public void setLattitude(double lattitude) {
        this.latitude = lattitude;
    }
}
