package com.example.saguaro.Bean;

import androidx.annotation.Nullable;


public class Localisation {

    private String uid;
    @Nullable private String urlPicture;
    private double longitude;
    private double latitude;

    public Localisation(@Nullable String urlPicture, double longitude, double latitude) {
        this.urlPicture = urlPicture;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double Latitude) {
        this.latitude = Latitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}