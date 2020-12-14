package com.example.dbmsproject3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForSpinners {
        private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BaseURL = "http://10.0.2.2:3000";

   public ForSpinners(){
       retrofit = new Retrofit.Builder()
               .baseUrl(BaseURL)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       retrofitInterface = retrofit.create(RetrofitInterface.class);

    }

    public ArrayList<String> SetSpinnnerBranch(final Spinner spinner, final Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
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


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, forSpinner);
                    spinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<BranchInfo> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return forSpinner;

    }

    public ArrayList<String> SetSpinnerCompany(final Spinner spinner,final Context context){
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

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, myCompanyInfo);
                spinner.setAdapter(arrayAdapter);

            }


            @Override
            public void onFailure(Call<ArrayList<RecieveCompanyInfo>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return myCompanyInfo;
    }

    public ArrayList<String> SetSpinnerCollege(final Spinner spinner,final Context context){
       final ArrayList<String> collegeNames = new ArrayList<String>();
        Call<ArrayList<CollegeInfo>> myColleges = retrofitInterface.getColleges();

        myColleges.enqueue(new Callback<ArrayList<CollegeInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CollegeInfo>> call, Response<ArrayList<CollegeInfo>> response) {
                if(response.code()==200){
                    ArrayList<CollegeInfo> myClgArray  = response.body();

                    for(CollegeInfo collegeInfo:myClgArray){
                        collegeNames.add(collegeInfo.getName());
                    }
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<String >(context,android.R.layout.simple_spinner_dropdown_item,collegeNames);
                    spinner.setAdapter(myAdapter);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CollegeInfo>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return collegeNames;

    }
}
