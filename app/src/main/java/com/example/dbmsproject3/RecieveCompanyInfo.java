package com.example.dbmsproject3;

public class RecieveCompanyInfo {
    private String name;
    private String tier;



    public  RecieveCompanyInfo(){}
        public  RecieveCompanyInfo(String s, String i)
        {

            name =s;
            tier=i;

        }
    public String getName() {
        return name;
    }

    public String getTier() {
        return tier;
    }
}
