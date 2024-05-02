package com.affc.ait;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        MaterialButton addCourseButton = findViewById(R.id.addCourse);
        MaterialButton addBranchButton = findViewById(R.id.addBranch);
        MaterialButton searchUsersButton = findViewById(R.id.searchUsers);
        Button adminLogOutButton = findViewById(R.id.adminLogOut);


        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, addCourses.class));
            }
        });

        addBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, AddBranch.class));
            }
        });

        searchUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, SearchUsers.class));
            }
        });

        adminLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, AdminLogin.class));
            }
        });
    }
}