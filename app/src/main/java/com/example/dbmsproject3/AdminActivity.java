package com.example.dbmsproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    int selected;

    private Retrofit retrofit;
    private String token="";
    private RetrofitInterface retrofitInterface;
    private String url = "https://mvt-placement-assistance.herokuapp.com/";


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button addCollege = findViewById(R.id.addCollege);
        Button addBranch = findViewById(R.id.addBranch);

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        addCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCollege();
            }
        });
        addBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBranch();
            }
        });
    }

    public void createCollege(){
        View view = getLayoutInflater().inflate(R.layout.college_register,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(AdminActivity.this);
        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        final EditText collegeName = view.findViewById(R.id.collegeName);
        final EditText collegeCode = view.findViewById(R.id.collegeCetcode);
        final EditText collegeLocation = view.findViewById(R.id.collegeLocation);
        final EditText collegePassword = view.findViewById(R.id.collegePassword);
        final EditText collegeNumber = view.findViewById(R.id.collegeContact);
        final EditText collegeDegree = view.findViewById(R.id.Degree);
       final Button submit = view.findViewById(R.id.submit);

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertDialog.dismiss();
               SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
               token= sharedPreferences.getString("Token","");
               HashMap<String,String> map = new HashMap<String, String>();
               map.put("name",collegeName.getText().toString());
               map.put("cetcode",collegeCode.getText().toString());
               map.put("degree",collegeDegree.getText().toString());
               map.put("contact",collegeNumber.getText().toString());
               map.put("location",collegeLocation.getText().toString());
               map.put("password",collegePassword.getText().toString());
               map.put("tokens",token);
              // Toast.makeText(AdminActivity.this, token, Toast.LENGTH_SHORT).show();



               Call<Void> call =retrofitInterface.sendCollegeInfo(map);
               call.enqueue(new Callback<Void>() {
                   @Override
                   public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(AdminActivity.this, "College Details Saved", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.code()==500)
                        {
                            Toast.makeText(AdminActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                        else  {
                            Toast.makeText(AdminActivity.this, "Unexpected error occured Information not saved", Toast.LENGTH_SHORT).show();
                        }
                   }

                   @Override
                   public void onFailure(Call<Void> call, Throwable t) {
                       Toast.makeText(AdminActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });

           }
       });


    }

    public void createBranch()
    {
        final View view = this.getLayoutInflater().inflate(R.layout.branch_register,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);

        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.show();
        final Button submit = view.findViewById(R.id.submitBranch);

        final EditText branchEdit = view.findViewById(R.id.branchEdit);
        Spinner spinner = view.findViewById(R.id.isCircuit);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               selected=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selected=0;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_2) {
                SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
                token= sharedPreferences.getString("Token","");

                alert.dismiss();
                EditText branchEdit = view.findViewById(R.id.branchEdit);
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("name",branchEdit.getText().toString());
                map.put("circuit",Integer.toString(selected));
                map.put("tokens",token);
                Call<Void> call = retrofitInterface.sendBranchInfo(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200)
                            Toast.makeText(AdminActivity.this, "Branch Added Successfully", Toast.LENGTH_SHORT).show();
                        else if(response.code()==400)
                            Toast.makeText(AdminActivity.this, "Branch already exists", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AdminActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }


}