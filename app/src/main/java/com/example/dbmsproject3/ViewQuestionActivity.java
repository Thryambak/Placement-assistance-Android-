package com.example.dbmsproject3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class ViewQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        Button filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.filter_spinners,null);
                AlertDialog.Builder builder= new AlertDialog.Builder(ViewQuestionActivity.this);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                if(alertDialog.isShowing()){
                    Spinner company = view.findViewById(R.id.spinnerCompany);
                    Spinner college = view.findViewById(R.id.spinnerCollege);
                    Spinner branch = view.findViewById(R.id.spinnerBranch);

                    ForSpinners myObj=new ForSpinners();
                    ArrayList<String> branchArray = myObj.SetSpinnnerBranch(branch,ViewQuestionActivity.this);
                    ArrayList<String> companyArray = myObj.SetSpinnerCompany(company, ViewQuestionActivity.this);
                    ArrayList<String> collegeArray = myObj.SetSpinnerCollege(college,ViewQuestionActivity.this);

                }
            }
        });




    }
}