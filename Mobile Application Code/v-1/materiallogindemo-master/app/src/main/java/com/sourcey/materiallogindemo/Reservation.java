package com.sourcey.materiallogindemo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alaa on 10/25/2018.
 */

public class Reservation implements Serializable {

    int resNo ;
    String zoneName, data ,carPlateNo , status ;
    double price;
    List<Integer> time;
    public Reservation() {
    }

    public Reservation(int resNo, String carPlateNo, String zoneName, String data, List<Integer> time, String status, double price) {
        this.resNo = resNo;
        this.carPlateNo = carPlateNo;
        this.zoneName = zoneName;
        this.data = data;
        this.time = time;
        this.status = status;
        this.price = price;
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

    public String getData() {
        return data;
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
}
