package com.example.dbmsproject3;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

public class CollegeInfo {

    private String name;

    private String location;
    private String degree;
    private String cetcode;
    private String contact;
    private String password;
    private String token;
    //private ArrayList<CollegeDisplayDetails> branches;
    private CollegeDisplayDetails[] branches;

    public CollegeDisplayDetails[] getBranches() {
        return branches;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDegree() {
        return degree;
    }

    public String getCetcode() {
        return cetcode;
    }

    public String getContact() {
        return contact;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
