package com.s2032257.tomschoonbeek.nasamarsrover.domain;

import java.io.Serializable;

public class MarsRoverPhoto implements Serializable{
    private int id;
    private int sol;
    private String earthDate;
    private String imageUrl;
    private String cameraName;
    private String cameraFullName;


    // Constructor

    public MarsRoverPhoto(int id, int sol, String earthDate, String imageUrl, String cameraName, String cameraFullName) {
        this.id = id;
        this.sol = sol;
        this.earthDate = earthDate;
        this.imageUrl = imageUrl;
        this.cameraName = cameraName;
        this.cameraFullName = cameraFullName;
    }


    // Getters en setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraFullName() {
        return cameraFullName;
    }

    public void setCameraFullName(String cameraFullName) {
        this.cameraFullName = cameraFullName;
    }
}
