package com.sourcey.materiallogindemo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alaa on 3/15/2019.
 */

public class Statistics {

   String day ;
   ArrayList<historyInfoPerDay> hoursInfo;

    public Statistics(String day, ArrayList<historyInfoPerDay> hoursInfo) {
        this.day = day;
        this.hoursInfo = hoursInfo;
    }

    public Statistics() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<historyInfoPerDay> getHoursInfo() {
        return hoursInfo;
    }

    public void setHoursInfo(ArrayList<historyInfoPerDay> hoursInfo) {
        this.hoursInfo = hoursInfo;
    }
}
