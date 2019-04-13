package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public  class Property  {

    //property basics

    private String zoneName;
    private int currentlyLooking;
    private String description;
    private String image;
    private int totalSpotsNo; // the total number of available spot in this zone


    //constructor


    public Property(String zoneName, int currentlyLooking, String description, String image, int totalSpotsNo) {
        this.zoneName = zoneName;
        this.currentlyLooking = currentlyLooking;
        this.description = description;
        this.image = image;
        this.totalSpotsNo = totalSpotsNo;
    }

    public Property(String zoneName, int currentlyLooking, int totalSpotsNo) {
        this.zoneName = zoneName;
        this.currentlyLooking = currentlyLooking;
        this.totalSpotsNo = totalSpotsNo;
    }

    public Property() {

    }

    //getters




    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }


    public String getZoneName() {
        return zoneName;
    }

    public int getCurrentlyLooking() {
        return currentlyLooking;
    }

    public int getTotalSpotsNo() {
        return totalSpotsNo;
    }


}