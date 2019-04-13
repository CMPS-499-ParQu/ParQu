package com.sourcey.materiallogindemo;

/**
 * Created by Alaa on 1/27/2019.
 */

public class Spot {
    String   zoneName;
    int status, spotNo;

    public Spot(String zoneName, int status, int spotNo) {
        this.zoneName = zoneName;
        this.status = status;
        this.spotNo = spotNo;
    }
    public Spot() {

    }

    public String getZoneName() {
        return zoneName;
    }

    public int getStatus() {
        return status;
    }

    public int getSpotNo() {
        return spotNo;
    }
}
