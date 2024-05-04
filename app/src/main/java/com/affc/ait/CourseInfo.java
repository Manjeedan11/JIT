package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;

import java.util.List;

public class CourseInfo extends AppCompatActivity {

    TextView courseNameView,startDateView, endDateView, feeView, descriptionView, maxParticipantsView, courseBranchesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Button btnRegister = findViewById(R.id.registerCourse);

        Intent intent = getIntent();
        int courseID = intent.getIntExtra("course_ID", -1);
        String courseName = intent.getStringExtra("course_name");
        String startDate = intent.getStringExtra("start_date");
        String endDate = intent.getStringExtra("end_date");
        double fee = intent.getDoubleExtra("fee", -1);
        String description = intent.getStringExtra("description");
        int maxParticipants = intent.getIntExtra("max_p", -1);

        courseNameView = findViewById(R.id.courseName);
        startDateView = findViewById(R.id.startingDate);
        endDateView = findViewById(R.id.endingDate);
        feeView = findViewById(R.id.fee);
        descriptionView = findViewById(R.id.description);
        maxParticipantsView = findViewById(R.id.maxpp);
        courseBranchesView = findViewById(R.id.branches);

        courseNameView.setText(courseName);
        startDateView.setText(startDate);
        endDateView.setText(endDate);
        feeView.setText(String.valueOf(fee));
        descriptionView.setText(description);
        maxParticipantsView.setText(String.valueOf(maxParticipants));
        searchForBranches(courseID);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                if(sharedPreferences.getInt("studentID", -1) != -1) {
                    Log.e("TAG", String.valueOf(sharedPreferences.getInt("studentID", -1)));
                    Intent intent = getIntent();
                    Intent starter = new Intent(CourseInfo.this, SelectBranch.class);
                    starter.putExtra("course_ID", intent.getIntExtra("course_ID", -1));
                    startActivity(starter);
                }
                else {
                    startActivity(new Intent(CourseInfo.this, Login.class));
                }
            }
        });

    }

    private void searchForBranches(int courseID) {
        DatabaseHandler db = new DatabaseHandler(this);

        List<Branch> branches = db.getBranchesForACourse(courseID);
        if (branches.size() > 0) {
            for (Branch branch : branches) {
                courseBranchesView.append(branch.getBranch_name() + "\n");
            }
        }
        else {
            courseBranchesView.setText("No branches found for this course");
        }


    }


}