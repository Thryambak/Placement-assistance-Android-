package com.example.dbmsproject3;

import com.google.gson.annotations.SerializedName;

public class BranchAttributes {
    public String name;
    public boolean circuit;
    @SerializedName("_id")
    public String id;
    public int avgPkg;;
}
