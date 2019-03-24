package com.sourcey.materiallogindemo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alaa on 10/25/2018.
 */

public class Reservation implements Serializable {

    int resNo ;
    String zoneName, date ,carPlateNo , status ;
    double price;
    List<Integer> time;
    int extendedHours, cancelledHours;
    public Reservation() {
    }

    public Reservation(int resNo, String carPlateNo, String zoneName, String data, List<Integer> time, String status, double price,  int extendedHours,int cancelledHours) {
        this.resNo = resNo;
        this.carPlateNo = carPlateNo;
        this.zoneName = zoneName;
        this.date = data;
        this.time = time;
        this.status = status;
        this.price = price;
        this.extendedHours = extendedHours;
        this.cancelledHours = cancelledHours;
    }

    public int getResNo() {
        return resNo;
    }

    public String getCarPlateNo() {
        return carPlateNo;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getDate() {
        return date;
    }

    public List<Integer> getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public int getExtendedHours() {
        return extendedHours;
    }

    public void setExtendedHours(int extendedHours) {
        this.extendedHours = extendedHours;
    }

    public int getCancelledHours() {
        return cancelledHours;
    }

    public void setCancelledHours(int cancelledHours) {
        this.cancelledHours = cancelledHours;
    }
}
