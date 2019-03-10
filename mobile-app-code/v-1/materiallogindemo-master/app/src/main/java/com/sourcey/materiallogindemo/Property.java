package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Property extends AppCompatActivity {

    //property basics
    private int zoneNumber;
    private String zoneName;
    private String description;
    private String image;
    private int currentAvailableNo; // the current number of available spots in a zone
    private int totalSpotsNo; // the total number of available spot in this zone
    private int enteredCarsNo; // the number of cars that currently parking in the zone ( reserved)
    private Boolean featured;

    //constructor
    public Property(
            int zoneNumber, String zoneName, String description, int currentAvilableNo, String image, int totalSpotsNo, int enteredCars, Boolean featured){

        this.zoneNumber = zoneNumber;
        this.zoneName = zoneName;
        this.description = description;
        this.currentAvailableNo = currentAvilableNo;
        this.image = image;
        this.totalSpotsNo = totalSpotsNo;
        this.enteredCarsNo = enteredCarsNo;
        this.featured = featured;
    }

    //getters
    public int getZoneNumber() { return zoneNumber; }
    public String getZoneName() {return zoneName; }
    public String getDescription() {return description; }
    public int getcurrentAvailableNo() {return currentAvailableNo; }
    public String getImage() { return image; }
    public int getAvailableSpotsNo(){ return totalSpotsNo; }
    public int getEnteredCarsNo(){ return enteredCarsNo; }
    public Boolean getFeatured(){return featured; }
}