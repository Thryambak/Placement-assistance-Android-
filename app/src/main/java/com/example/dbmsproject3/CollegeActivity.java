package com.example.dbmsproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.effect.Effect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private int selected = 0;
    private String BaseURL = "http://10.0.2.2:3000";
    int ItemSelected = 0;
    private  String Company="";
    boolean isCompanyNeeded = false;
   private int companySelected;
    private int branchSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);
        Button addVisit = findViewById(R.id.addVisit);
        Button addClgButton = findViewById(R.id.branchAdd);
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        addClgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBranchInfo();
            }
        });
        Button newbutton = findViewById(R.id.addQuestion);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
            }
        });
        addVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVisitFunc();
            }
        });
    }

    public void sendBranchInfo() {

        final View view = this.getLayoutInflater().inflate(R.layout.branch_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.show();

        final Spinner spinner = view.findViewById(R.id.branchSpinner);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        final String token = sharedPreferences.getString("Token", "");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        Call<BranchInfo> call = retrofitInterface.getBranchInfo(map);
        call.enqueue(new Callback<BranchInfo>() {
            @Override
            public void onResponse(Call<BranchInfo> call, Response<BranchInfo> response) {
                if (response.code() == 200) {

                    BranchInfo branchInfo = response.body();
                    final ArrayList<BranchAttributes> branchAttributes = branchInfo.getFinals();
                    final ArrayList<String> forSpinner = new ArrayList<String>();
                    Toast.makeText(CollegeActivity.this, "Branch Sizes" + branchAttributes.size(), Toast.LENGTH_SHORT).show();
                    for (int k = 0; k < branchAttributes.size(); k++) {
                        forSpinner.add(branchAttributes.get(k).name);
                        Log.i("Name", branchAttributes.get(k).name);
                    }


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, forSpinner);
                    spinner.setAdapter(arrayAdapter);


                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selected = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            selected=0;
                        }
                    });

                    Button submit = view.findViewById(R.id.branchSubmit);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            alert.dismiss();
                            HashMap<String, String> myMap = new HashMap<String, String>();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            ;

                            String cetcode = sharedPreferences.getString("Activeuser", "");
                            HashMap<String, String> map = new HashMap<String, String>();
                            myMap.put("cetcode", cetcode);
                            myMap.put("branches", branchAttributes.get(selected).id);
                            myMap.put("token", token);
                            Call<Void> call1 = retrofitInterface.addHasBranch(myMap);
                            call1.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() == 200)
                                        Toast.makeText(CollegeActivity.this, "Added Branch Successfully", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(CollegeActivity.this, "Error while adding branch", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<BranchInfo> call, Throwable t) {
                Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void addQuestion() {

        final View view = this.getLayoutInflater().inflate(R.layout.questions_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.show();


        final Spinner spinner = view.findViewById(R.id.Company);
        HashMap<String, String> map = new HashMap<String, String>();
        Call<ArrayList<RecieveCompanyInfo>> call = retrofitInterface.getCompanies(map);
        call.enqueue(new Callback<ArrayList<RecieveCompanyInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<RecieveCompanyInfo>> call, Response<ArrayList<RecieveCompanyInfo>> response) {
                final ArrayList<RecieveCompanyInfo> companyInfos = response.body();

                final ArrayList<String> myCompanyInfo = new ArrayList<String>();
                int size = 0;
                if (companyInfos == null) {
                    size = 0;
                } else size = companyInfos.size();
                for (int k = 0; k < size; k++) {
                    myCompanyInfo.add(companyInfos.get(k).getName());
                }
                myCompanyInfo.add("Add New Company");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, myCompanyInfo);
                spinner.setAdapter(arrayAdapter);
                final int mySize = size;

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view2, int i, long l) {
                        Toast.makeText(CollegeActivity.this, myCompanyInfo.get(i), Toast.LENGTH_SHORT).show();
                        ItemSelected = i;
                        EditText companyName = view.findViewById(R.id.companyName);
                        EditText companyTier = view.findViewById(R.id.companyTier);
                        if (i == mySize) {
                            isCompanyNeeded = true;
                            companyName.setVisibility(View.VISIBLE);
                            companyTier.setVisibility(View.VISIBLE);
                        } else {
                            isCompanyNeeded = false;
                            companyName.setVisibility(View.INVISIBLE);
                            companyTier.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        ItemSelected = 0;
                    }
                });

                createNewQuestion(view, myCompanyInfo.get(ItemSelected),alert);


            }

            @Override
            public void onFailure(Call<ArrayList<RecieveCompanyInfo>> call, Throwable t) {
                Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void addVisitFunc(){

        View myView= getLayoutInflater().inflate(R.layout.addvisit,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(myView);
        AlertDialog alert = builder.create();
        alert.show();
        final Button submit= myView.findViewById(R.id.buttonAddVisit);
        final EditText year= myView.findViewById(R.id.year);
        final EditText noSel = myView.findViewById(R.id.noSelected);
        final EditText avgpkg = myView.findViewById(R.id.avgpkg);
        final Spinner spinner = myView.findViewById(R.id.CompanySpinner);
        final Spinner branchSpinner= myView.findViewById(R.id.BranchSpinner);
        final ArrayList<String> company = SetSpinnerCompany(spinner);
        final ArrayList<String> branches = SetSpinnnerBranch(branchSpinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                companySelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                companySelected=0;
            }
        });

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchSelected=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                branchSelected=0;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map = new HashMap<String, String>();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final String token = sharedPreferences.getString("Token", "");
                map.put("token", token);
                Log.i("TOKENS ARE",token);
                map.put("company",company.get(companySelected));
                map.put("branch",branches.get(branchSelected));
                map.put("averagePackage",avgpkg.getText().toString());
                map.put("year",year.getText().toString());
                map.put("numberOfSelected",noSel.getText().toString());
                Call<Void> myCall= retrofitInterface.addVisit(map);
                myCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(CollegeActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.code()==400)Toast.makeText(CollegeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public void createNewQuestion(final View myView, final String companyName, final AlertDialog alertDialog) {
        final Spinner spinner = myView.findViewById(R.id.branch);
        Company=companyName;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ;
        final String token = sharedPreferences.getString("Token", "");
        final HashMap<String, String> map = new HashMap<String, String>();
        final ArrayList<String> forSpinner = new ArrayList<String>();
        map.put("token", token);
        map.put("isUnique", "no");
        Call<BranchInfo> call = retrofitInterface.getBranchInfo(map);
        call.enqueue(new Callback<BranchInfo>() {
            @Override
            public void onResponse(Call<BranchInfo> call, Response<BranchInfo> response) {
                if (response.code() == 200) {

                    BranchInfo branchInfo = response.body();
                    final ArrayList<BranchAttributes> branchAttributes = branchInfo.getFinals();


                    for (int k = 0; k < branchAttributes.size(); k++) {
                        forSpinner.add(branchAttributes.get(k).name);
                        Log.i("Name", branchAttributes.get(k).name);
                    }


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, forSpinner);
                    spinner.setAdapter(arrayAdapter);


                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                           selected = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            selected=0;
                        }

                    });

                   map.put("myBranch",branchAttributes.get(selected).id);

                }
            }

            @Override
            public void onFailure(Call<BranchInfo> call, Throwable t) {

            }
        });


        final EditText Round = myView.findViewById(R.id.round);
        final EditText Topic = myView.findViewById(R.id.topic);
        final EditText Question = myView.findViewById(R.id.descreption);
        final String Branch = map.get("myBranch");
        final View myView2 = myView;


        final Button submit = myView.findViewById(R.id.questionSubmit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCompanyNeeded) {

                    final HashMap<String, String> newmap = new HashMap<String, String>();
                    EditText newCompanyName = myView2.findViewById(R.id.companyName);
                    EditText newCompanyTier = myView2.findViewById(R.id.companyTier);
                    Log.i("Company name",newCompanyName.getText().toString());
                    newmap.put("token",token);

                    newmap.put("name", newCompanyName.getText().toString());
                    newmap.put("tier", newCompanyTier.getText().toString());
                    Log.i("NAME   ",newCompanyName.getText().toString());
                    Log.i("Tier  ", newCompanyTier.getText().toString());

                    Call<Void> call1 = retrofitInterface.addCompany(newmap);
                  call1.enqueue(new Callback<Void>() {
                      @Override
                      public void onResponse(Call<Void> call, Response<Void> response) {
                          if(response.code()==200)
                          {
                              Company = newmap.get("name");

                             // Toast.makeText(CollegeActivity.this, "New Company added", Toast.LENGTH_SHORT).show();

                          }
                      }

                      @Override
                      public void onFailure(Call<Void> call, Throwable t) {
                          Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });




                }

               final  EditText roundEdit = myView.findViewById(R.id.round);
                final EditText topicEdit= myView.findViewById(R.id.topic);
                final EditText questionEdit= myView.findViewById(R.id.descreption);
                final String branch = forSpinner.get(selected);
                HashMap<String,String> questMap = new HashMap<String, String>();
                questMap.put("branch",branch);
                questMap.put("companyname",Company);
                questMap.put("round",roundEdit.getText().toString());
                questMap.put("topic",topicEdit.getText().toString());
                questMap.put("description",questionEdit.getText().toString());
                questMap.put("token",token);
                questMap.put("auth","college");
                Call<Void> questCall = retrofitInterface.addQuestion(questMap);
                questCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200)
                        {
                            Toast.makeText(CollegeActivity.this, "Question added", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });



    }


    public ArrayList<String> SetSpinnerCompany(final Spinner spinner){
        HashMap<String, String> map = new HashMap<String, String>();
        final ArrayList<String> myCompanyInfo = new ArrayList<String>();
        Call<ArrayList<RecieveCompanyInfo>> call = retrofitInterface.getCompanies(map);
        call.enqueue(new Callback<ArrayList<RecieveCompanyInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<RecieveCompanyInfo>> call, Response<ArrayList<RecieveCompanyInfo>> response) {
                final ArrayList<RecieveCompanyInfo> companyInfos = response.body();


                int size = companyInfos.size();
                for (int k = 0; k < size; k++) {
                    myCompanyInfo.add(companyInfos.get(k).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, myCompanyInfo);
                spinner.setAdapter(arrayAdapter);

            }


            @Override
            public void onFailure(Call<ArrayList<RecieveCompanyInfo>> call, Throwable t) {
                Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return myCompanyInfo;
    }

    public  ArrayList<String> SetSpinnnerBranch(final Spinner spinner){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String token = sharedPreferences.getString("Token", "");
        final HashMap<String, String> map = new HashMap<String, String>();
        final ArrayList<String> forSpinner = new ArrayList<String>();
        map.put("token", token);
        map.put("isUnique", "no");
        Call<BranchInfo> call = retrofitInterface.getBranchInfo(map);
        call.enqueue(new Callback<BranchInfo>() {
            @Override
            public void onResponse(Call<BranchInfo> call, Response<BranchInfo> response) {
                if (response.code() == 200) {

                    BranchInfo branchInfo = response.body();
                    final ArrayList<BranchAttributes> branchAttributes = branchInfo.getFinals();


                    for (int k = 0; k < branchAttributes.size(); k++) {
                        forSpinner.add(branchAttributes.get(k).name);
                        Log.i("Name", branchAttributes.get(k).name);
                    }


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, forSpinner);
                    spinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<BranchInfo> call, Throwable t) {
                Toast.makeText(CollegeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return forSpinner;

    }



}



