package com.example.dbmsproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestionActivity extends AppCompatActivity {
    private int collegeSelected;
    private int companySelected;
    private int branchSelected;
    private Retrofit retrofit;
    public static Questions[] myQuestions;

    private RetrofitInterface retrofitInterface;
    private String BaseURL = "https://mvt-placement-assistance.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        final TextView collegeText = findViewById(R.id.textViewCollege);
        final TextView branchText = findViewById(R.id.textViewBranch);
        final TextView companyText = findViewById(R.id.textViewCompany);
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        collegeText.setText("College : All");
        branchText.setText("Branch : All");
        companyText.setText("Company : All");

        Button filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.filter_spinners,null);
                AlertDialog.Builder builder= new AlertDialog.Builder(ViewQuestionActivity.this);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                if(alertDialog.isShowing()){
                    final Spinner company = view.findViewById(R.id.spinnerCompany);
                    final Spinner college = view.findViewById(R.id.spinnerCollege);
                    final Spinner branch = view.findViewById(R.id.spinnerBranch);
                    Button apply = view.findViewById(R.id.buttonApply);
                    company.setSelection(companySelected);
                    branch.setSelection(branchSelected);
                    college.setSelection(companySelected);

                    ForSpinners myObj=new ForSpinners();
                    final ArrayList<String> branchArray = myObj.SetSpinnnerBranch(branch,ViewQuestionActivity.this);
                    final ArrayList<String> companyArray = myObj.SetSpinnerCompany(company, ViewQuestionActivity.this);
                    final ArrayList<String> collegeArray = myObj.SetSpinnerCollege(college,ViewQuestionActivity.this);

                    //OnSelected Listeners;
                    company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            companySelected=position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            companySelected=0;
                        }
                    });
                    branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            branchSelected=position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            branchSelected=0;
                        }
                    });
                    college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            collegeSelected = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            collegeSelected=0;
                        }
                    });

                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String myCollege,myBranch,myCompany;
                            if(collegeSelected==0) myCollege="";
                            else myCollege=collegeArray.get(collegeSelected);
                            collegeText.setText("College : "+collegeArray.get(collegeSelected));


                            if(branchSelected==0) myBranch="";
                            else myBranch=branchArray.get(branchSelected);
                            branchText.setText("Branch : "+branchArray.get(branchSelected));


                            if(companySelected==0) myCompany="";
                            else myCompany=companyArray.get(companySelected);
                            companyText.setText("Company :"+companyArray.get(companySelected));



                            final ListView myList = findViewById(R.id.list);
                            final ArrayList<String> arrayList = new ArrayList<String>();

                            Call<Questions[]> call = retrofitInterface.viewQuetions(myCollege,myBranch,myCompany);
                            call.enqueue(new Callback<Questions[]>() {
                                @Override
                                public void onResponse(Call<Questions[]> call, Response<Questions[]> response) {

                                    Questions[] questions = response.body();
                                    myQuestions= response.body();
                                    Log.i("DEBUG PLS",response.body().toString());


                                    for(Questions q : questions){
                                        arrayList.add(q.getTopic());
                                    }
                                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.textfield,arrayList);
                                    myList.setAdapter(arrayAdapter);


                                }

                                @Override
                                public void onFailure(Call<Questions[]> call, Throwable t) {
                                    Toast.makeText(ViewQuestionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.i("Debug plss",t.getMessage());
                                }
                            });
                       alertDialog.dismiss(); }
                    });
                }
            }
        });
        final ListView myList = findViewById(R.id.list);
        final ArrayList<String> arrayList = new ArrayList<String>();

        Call<Questions[]> call = retrofitInterface.viewQuetions("","","");
        call.enqueue(new Callback<Questions[]>() {
            @Override
            public void onResponse(Call<Questions[]> call, Response<Questions[]> response) {

                Questions[] questions = response.body();
                myQuestions= response.body();
                Log.i("DEBUG PLS",response.body().toString());


                for(Questions q : questions){
                    arrayList.add(q.getTopic());
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.textfield,arrayList);
                myList.setAdapter(arrayAdapter);


            }

            @Override
            public void onFailure(Call<Questions[]> call, Throwable t) {
                Toast.makeText(ViewQuestionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Debug plss",t.getMessage());
            }
        });



        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getApplicationContext(),ViewEachQuestion.class);
                myIntent.putExtra("position",position);
                startActivity(myIntent);
            }
        });

    }

    public Questions getCurrentQuestion(int position){
       if(myQuestions != null) return myQuestions[position];
        return null;
    }
}