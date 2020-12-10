package com.example.dbmsproject3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DisplayBranchClg extends AppCompatActivity {
RecycleViewAdapter recycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_branch_clg);
        CollegeInfo hereClgInfo= ViewActivity.myCollege;
//        CollegeDisplayDetails[] myClgDet=hereClgInfo.getBranches();
//        for(CollegeDisplayDetails clg:myClgDet){
//            Log.i("NEW PRINT",clg.getBranchid().name);
//        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView collegeName= findViewById(R.id.collegeName);
        TextView collegeLocation=findViewById(R.id.collegeLocation);
        collegeName.setText(hereClgInfo.getName());
        collegeLocation.setText(hereClgInfo.getLocation());

        recycleViewAdapter = new RecycleViewAdapter(this,hereClgInfo);
        recyclerView.setAdapter(recycleViewAdapter);






    }
}