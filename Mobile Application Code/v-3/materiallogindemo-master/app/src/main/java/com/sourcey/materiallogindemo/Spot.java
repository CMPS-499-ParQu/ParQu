package com.sourcey.materiallogindemo;

/**
 * Created by Alaa on 1/27/2019.
 */

public class Spot {
    String   zoneName;
    int  spotNo;
    String status;

    public Spot(String zoneName, String status, int spotNo) {
        this.zoneName = zoneName;
        this.status = status;
        this.spotNo = spotNo;
    }
    public Spot() {

    }

    public String getZoneName() {
        return zoneName;
    }

    public String getStatus() {
        return status;
    }

    public int getSpotNo() {
        return spotNo;
    }
}
