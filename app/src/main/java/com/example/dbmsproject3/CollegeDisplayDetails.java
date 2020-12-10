package com.example.dbmsproject3;

import java.util.ArrayList;

public class CollegeDisplayDetails {

    private String name;
    private boolean circuit;

    public Placed[] getPlaced() {
        return placed;
    }

    public int getAveragePackage() {
        return averagePackage;
    }

    private Placed[] placed;
//    private int totalPlaced;
    private int averagePackage;

    private BranchAttributes branchid;

    public BranchAttributes getBranchid() {
        return branchid;
    }

    public String getName() {
        return name;
    }

    public boolean isCircuit() {
        return circuit;
    }
//
//    public Placed[] getStudentsPlaced() {
//        return studentsPlaced;
//    }
//
//    public int getTotalPlaced() {
//        return totalPlaced;
//    }

    public int getAvgPkg() {
        return avgPkg;
    }

    private int avgPkg;

}
