package com.example.dbmsproject3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewEachQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_each_question);
        TextView round = findViewById(R.id.round);
        TextView topic = findViewById(R.id.topic);
        TextView description = findViewById(R.id.description);
        Intent myIntent = getIntent();
        int position = myIntent.getIntExtra("position",0);


       if( ViewQuestionActivity.myQuestions ==null)
           description.setText("Error Try again");



       else{
           Questions myQuestions = ViewQuestionActivity.myQuestions[position];
           round.setText(myQuestions.getRound());
           topic.setText(myQuestions.getTopic());
           description.setText(myQuestions.getDescription());
       }




    }
}