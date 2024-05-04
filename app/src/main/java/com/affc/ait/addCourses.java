package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;
import com.affc.ait.models.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class addCourses extends AppCompatActivity {

    private EditText editTextCourseName, editTextDescription, editTextStartDate, editTextEndDate,
            editTextCourseFee, editTextMaxParticipants, editTextPublishDate;
    private Spinner spinnerBranches;
    private Button btnAddCourse;
    private LinearLayout selectedBranchesLayout;
    private List<Branch> selectedBranches = new ArrayList<>();
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);

        btnAddCourse = findViewById(R.id.btnAddCourse);
        editTextCourseName = findViewById(R.id.courseName);
        editTextDescription = findViewById(R.id.description);
        editTextStartDate = findViewById(R.id.startDate);
        editTextEndDate = findViewById(R.id.endsDate);
        editTextCourseFee = findViewById(R.id.courseFee);
        editTextMaxParticipants = findViewById(R.id.maxpp);
        editTextPublishDate = findViewById(R.id.publishDate);
        spinnerBranches = findViewById(R.id.branches);
        selectedBranchesLayout = findViewById(R.id.selectedBranchesLayout);

        loadBranches();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        btnAddCourse.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String courseName = editTextCourseName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String startDateStr = editTextStartDate.getText().toString().trim();
                String endDateStr = editTextEndDate.getText().toString().trim();
                String courseFeeStr = editTextCourseFee.getText().toString().trim();
                String maxParticipantsStr = editTextMaxParticipants.getText().toString().trim();
                String publishDateStr = editTextPublishDate.getText().toString().trim();

                // Validate input fields
                if (courseName.isEmpty() || description.isEmpty() || startDateStr.isEmpty() ||
                        endDateStr.isEmpty() || courseFeeStr.isEmpty() || maxParticipantsStr.isEmpty() ||
                        publishDateStr.isEmpty() || selectedBranches.isEmpty()) {
                    Toast.makeText(addCourses.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double courseFee = Double.parseDouble(courseFeeStr);
                int maxParticipants = Integer.parseInt(maxParticipantsStr);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date startDate, endDate, publishDate;
                try {
                    startDate = dateFormat.parse(startDateStr);
                    endDate = dateFormat.parse(endDateStr);
                    publishDate = dateFormat.parse(publishDateStr); // Parse the publish date


                    if (endDate.before(startDate)) {
                        Toast.makeText(addCourses.this, "End date must be after start date!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (publishDate.after(startDate)) {
                        Toast.makeText(addCourses.this, "Publish date must be before the start date!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (ParseException e) {
                    Toast.makeText(addCourses.this, "Invalid date format!", Toast.LENGTH_SHORT).show();
                    return;
                }


                Course course = new Course(courseName, startDateStr, endDateStr,courseFee, description, maxParticipants);
                course = new Course(courseName, startDate.toString(), endDate.toString(), courseFee, description, maxParticipants);
                addCourseToDatabase(course);



                NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                        .setSmallIcon(R.drawable.courses)
                        .setContentTitle("AIT Institute")
                        .setContentText("Course: " + course.getCourse_name() + " is starting on " + course.getStart_date() + " sign up now!");
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, mbuilder.build());
            }
        });
    }



    private void loadBranches() {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        List<Branch> branches = dbHandler.fetchBranches();

        List<String> branchNames = new ArrayList<>();
        for (Branch branch : branches) {
            branchNames.add(branch.getBranch_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branchNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranches.setAdapter(adapter);

        spinnerBranches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Add selected branch to the list
                Branch selectedBranch = branches.get(position);
                selectedBranches.add(selectedBranch);

                // Display selected branch
                addSelectedBranch(selectedBranch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void addSelectedBranch(Branch branch) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 20, 0);
        textView.setLayoutParams(params);
        textView.setPadding(10, 5, 10, 5);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText(branch.getBranch_name());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBranchesLayout.removeView(textView);
                selectedBranches.remove(branch);
            }
        });

        selectedBranchesLayout.addView(textView);
    }

    private void addCourseToDatabase(Course course) {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        long courseId = dbHandler.addCourse(course);

        if (courseId != -1) {
            for (Branch branch : selectedBranches) {
                int branchId = dbHandler.getBranchIdByName(branch.getBranch_name());
                int result = dbHandler.addCourseToBranch((int) courseId, branchId);
                if (result == -1) {
                    // If adding course to branch fails, you may handle it here
                    Toast.makeText(this, "Failed to add course to branch!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add course!", Toast.LENGTH_SHORT).show();
        }
    }


}
