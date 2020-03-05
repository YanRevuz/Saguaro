package com.example.saguaro.Bean;

import androidx.annotation.Nullable;

import java.util.UUID;

public class Localisation {

    private String uid;
    @Nullable private String urlPicture;
    private double longitude;
    private double latitude;

    public Localisation(String uid, @Nullable String urlPicture, double longitude, double latitude) {
        this.uid = uid;
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

    public double getLattitude() {
        return latitude;
    }

    public void setLattitude(double lattitude) {
        this.latitude = lattitude;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
