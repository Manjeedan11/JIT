package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;
import com.affc.ait.models.Course;

public class ConfirmCourse extends AppCompatActivity {

    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_course);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        TextView container = findViewById(R.id.confirm_course_text);

        Intent intent = getIntent();
        Branch branch = databaseHandler.getBranchByID(intent.getIntExtra("branch_ID", -1));
        Course course = databaseHandler.getCourseByID(intent.getIntExtra("course_ID", -1));

        body= "Course: " + course.getCourse_name() + "\n" +
                "Branch: " + branch.getBranch_name() + "\n" +
                "Start Date: " + course.getStart_date() + "\n" +
                "End Date: " + course.getEnd_date() + "\n" +
                "Fee: " + course.getFee() + "\n" +
                "Description: " + course.getDescription() + "\n" +
                "Max Participants: " + course.getMax_p() + "\n" +
                "Branch Address: " + branch.getLocation() + "\n";

        container.setText(body);
    }

    public void performEnroll(View view) {

    }

    public void sendEmail(){

    }


}