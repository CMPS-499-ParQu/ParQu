package com.sourcey.materiallogindemo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Alaa on 10/23/2018.
 */

public class User {

   private  String name, plateNo , email, mobile , password , uid;



        public User(){

        }


    public User(String name, String plateNo, String email, String mobile, String password, String uid) {
        this.name = name;
        this.plateNo = plateNo;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
