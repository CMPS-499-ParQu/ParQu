package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

public  class Property implements Serializable {

    //property basics

    private String zoneName;
    private String description;
    private String image;
    private int totalSpotsNo; // the total number of available spot in this zone
    private ArrayList<Statistics> statistics;

    //constructor


    public Property(String zoneName, String description, String image, int totalSpotsNo) {
        this.zoneName = zoneName;
        this.description = description;
        this.image = image;
        this.totalSpotsNo = totalSpotsNo;
    }

    public Property(String zoneName, int totalSpotsNo, ArrayList<Statistics> statistics) {
        this.zoneName = zoneName;
        this.totalSpotsNo = totalSpotsNo;
        this.statistics = statistics;
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


    public int getTotalSpotsNo() {
        return totalSpotsNo;
    }



    public void setTotalSpotsNo(int totalSpotsNo) {
        this.totalSpotsNo = totalSpotsNo;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Statistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(ArrayList<Statistics> statistics) {
        this.statistics = statistics;
    }
}