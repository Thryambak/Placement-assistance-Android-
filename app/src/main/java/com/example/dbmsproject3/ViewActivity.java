package com.example.dbmsproject3;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    public static CollegeInfo myCollege;

    private Retrofit retrofit;
    private   RetrofitInterface retrofitInterface;
    private   String BaseURL="http://10.0.2.2:3000";
    ListView listView;

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
            case 1: ViewQuestions();
                    break;
//

        }





    }

    protected void ViewColleges(){

        Call<ArrayList<CollegeInfo>> call = retrofitInterface.getColleges();

       call.enqueue(new Callback<ArrayList<CollegeInfo>>() {
           @Override
           public void onResponse(Call<ArrayList<CollegeInfo>> call, Response<ArrayList<CollegeInfo>> response) {
               if(response.code()!=200){
                    Toast.makeText(ViewActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    finish();
               }
               else{
                   listView = findViewById(R.id.List);
                   final ArrayList<CollegeInfo> myClgInfo= response.body();
                   final ArrayList<String> clgName=new ArrayList<String>();
                   for(CollegeInfo collegeInfo:myClgInfo){

                       clgName.add(collegeInfo.getName());
                   }
                   final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewActivity.this,R.layout.textfield,clgName);
                   listView.setAdapter(arrayAdapter);
                   AutoCompleteTextView searchEdit = findViewById(R.id.search);
                   searchEdit.setAdapter(arrayAdapter);
                   searchEdit.setThreshold(1);
                   searchEdit.setDropDownHeight(0);

                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           int index = clgName.indexOf(arrayAdapter.getItem(position));
                             String cetCode =   myClgInfo.get(index).getCetcode();

                         Call<CollegeInfo> myCall = retrofitInterface.getClgInfo("/college/details/"+cetCode);

                         myCall.enqueue(new Callback<CollegeInfo>() {
                             @Override
                             public void onResponse(Call<CollegeInfo> call, Response<CollegeInfo> response) {
                                 if(response.code()==200){

                                     CollegeInfo collegeInfo = response.body();
                                     myCollege=collegeInfo;
                                        Intent myIntent=new Intent(getApplicationContext(),DisplayBranchClg.class);
                                        startActivity(myIntent);

//                                        CollegeDisplayDetails[] myBranchArray = collegeInfo.getBranches();
//                                        for(CollegeDisplayDetails clg :myBranchArray) {
//
//                                        }
//                                        for(CollegeDisplayDetails clg :myBranchArray) {
//                                            Log.i("debug",clg.getName());
//                                        }
                                 }
                             }

                             @Override
                             public void onFailure(Call<CollegeInfo> call, Throwable t) {
                                 Toast.makeText(ViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                             }
                         });


                       }
                   });



               }
           }

           @Override
           public void onFailure(Call<ArrayList<CollegeInfo>> call, Throwable t) {
               Toast.makeText(ViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
               finish();
           }
       });


    }

    protected void ViewQuestions(){
        Intent intent = new Intent(getApplicationContext(),ViewQuestionActivity.class);
        startActivity(intent);
        finish();
    }
}