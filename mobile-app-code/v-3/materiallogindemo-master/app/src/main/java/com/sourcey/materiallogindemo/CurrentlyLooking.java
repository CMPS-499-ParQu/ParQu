package com.sourcey.materiallogindemo;

import java.util.Date;

/**
 * Created by Alaa on 2/25/2019.
 */

public class CurrentlyLooking {

    int ID;
    int hour, minutes;
    String zoneName;

    public CurrentlyLooking(int ID, int hour, int minutes, String zoneName) {
        this.ID = ID;
        this.hour = hour;
        this.minutes = minutes;
        this.zoneName = zoneName;
    }

    public CurrentlyLooking() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
