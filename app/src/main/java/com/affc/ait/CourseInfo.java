package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Button;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;
import com.affc.ait.models.Course;

import java.util.List;

public class CourseInfo extends AppCompatActivity {

    TextView courseNameView, startDateView, endDateView, feeView, descriptionView, maxParticipantsView, courseBranchesView;

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

        ImageButton button = findViewById(R.id.hamburger);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(CourseInfo.this, button);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.nav_menu_silder, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        String title = (String) menuItem.getTitle();
                        if (title.equals("Login")) {
                            Intent intent = new Intent(CourseInfo.this, Login.class);
                            startActivity(intent);
                        } else if (title.equals("Register")) {
                            Intent intent = new Intent(CourseInfo.this, Register.class);
                            startActivity(intent);
                        } else if (title.equals("Courses")) {
                            Intent intent = new Intent(CourseInfo.this, Home.class);
                            startActivity(intent);
                        } else if (title.equals("Logout")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            Log.e("email", "onMenuItemClick: " + sharedPreferences.getString("email", "") + sharedPreferences.getInt("studentID", -1));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //clear preferences
                            editor.clear();
                            editor.apply();
                            finish();
                            Intent intent = new Intent(CourseInfo.this, Login.class);
                            startActivity(intent);
                        } else if (title.equals("Admin")) {
                            Intent intent = new Intent(CourseInfo.this, AdminLogin.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                if (sharedPreferences.getInt("studentID", -1) != -1) {
                    Log.e("TAG", String.valueOf(sharedPreferences.getInt("studentID", -1)));
                    Intent intent = getIntent();
                    Intent starter = new Intent(CourseInfo.this, SelectBranch.class);
                    starter.putExtra("course_ID", intent.getIntExtra("course_ID", -1));
                    startActivity(starter);
                } else {
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
        } else {
            courseBranchesView.setText("No branches found for this course");
        }


    }


}