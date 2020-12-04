package com.example.dbmsproject3;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private   RetrofitInterface retrofitInterface;
    private   String BaseURL="http://10.0.2.2:3000";
    ListView listView = findViewById(R.id.List);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        int option = intent.getIntExtra("option",-1);

        retrofit= new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface= retrofit.create(RetrofitInterface.class);

        switch (option){
            case 0: ViewColleges();
                    break;
//            case 1: ViewQuestions();
//                    break;
//            case 2: ViewCompanys();
//                    break;
//            case 3: ViewBranches();

        }





    }

    protected void ViewColleges(){

        Call<ArrayList<CollegeInfo>> call = retrofitInterface.getColleges();

       call.enqueue(new Callback<ArrayList<CollegeInfo>>() {
           @Override
           public void onResponse(Call<ArrayList<CollegeInfo>> call, Response<ArrayList<CollegeInfo>> response) {
               if(response.code()!=200)
                   Toast.makeText(ViewActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
               else{
                   ArrayList<CollegeInfo> myClgInfo= response.body();
                   ArrayList<String> clgName=new ArrayList<String>();
                   for(CollegeInfo collegeInfo:myClgInfo){

                       clgName.add(collegeInfo.getName());
                   }
                   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewActivity.this,android.R.layout.simple_list_item_1,clgName);
                   listView.setAdapter(arrayAdapter);


               }
           }

           @Override
           public void onFailure(Call<ArrayList<CollegeInfo>> call, Throwable t) {

           }
       });


    }

}