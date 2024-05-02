package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CourseInfo extends AppCompatActivity {

    TextView courseNameView, courseBranchesView, startDateView, endDateView, feeView, descriptionView, maxParticipantsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Intent intent = getIntent();
        int courseID = intent.getIntExtra("course_ID", -1);
        String courseName = intent.getStringExtra("course_name");
        String startDate = intent.getStringExtra("start_date");
        String endDate = intent.getStringExtra("end_date");
        double fee = intent.getDoubleExtra("fee", -1);
        String description = intent.getStringExtra("description");
        int maxParticipants = intent.getIntExtra("max_p", -1);

        courseNameView = findViewById(R.id.courseName);
        startDateView = findViewById(R.id.courseStarting);
        endDateView = findViewById(R.id.closingRegDate);
        feeView = findViewById(R.id.courseFee);
        descriptionView = findViewById(R.id.description);
        maxParticipantsView = findViewById(R.id.maxpp);
        courseBranchesView = findViewById(R.id.courseBranches);


    }
}