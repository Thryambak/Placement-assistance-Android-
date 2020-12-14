package com.example.dbmsproject3;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitInterface {


    @POST("/add/has")
    Call<Void> addHasBranch(@Body HashMap<String,String> map);

    @POST("/login/college")
    Call<RecieveInfo> requestCollegeLogin (@Body HashMap<String,String> map);

    @POST("/login/admin")
    Call<RecieveInfo> requestAdminLogin (@Body HashMap<String,String> map);

    @POST("/collegeRegister")
    Call<Void> requestCollegeRegister (@Body HashMap<String ,String> map);

    @POST("/add/college")
    Call<Void> sendCollegeInfo (@Body HashMap<String,String> map);

    @POST("/add/branch")
    Call<Void> sendBranchInfo (@Body HashMap<String,String> map);
    @POST("/branches/data")
  Call<BranchInfo> getBranchInfo(@Body HashMap<String,String> map);

    @POST("/companies")
    Call<ArrayList<RecieveCompanyInfo>> getCompanies(@Body HashMap<String,String> map);

    @POST("/choose/company")
    Call<Void>  addCompany(@Body HashMap<String,String> map);
    @POST("/add/question")
    Call<Void>  addQuestion(@Body HashMap<String,String> map);

    @GET("/questions")
    Call<Void> viewQuetions(@Query("college") String college ,@Query("branch") String branch,@Query("company") String company);

    @GET("/colleges")
    Call<ArrayList<CollegeInfo>> getColleges();

    @GET()
    Call<CollegeInfo> getClgInfo(@Url String url);

    @POST("/add/visits")
    Call<Void> addVisit(@Body HashMap<String,String> map);



}
