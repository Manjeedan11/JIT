package com.affc.ait;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;
import com.affc.ait.models.Course;

import java.util.List;

public class SelectBranch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        List<Course> courses = databaseHandler.fetchCourses();

        renderBranchesForCourses(courses);
    }

    private void renderBranchesForCourses(List<Course> courses) {
        LinearLayout branchLayout = findViewById(R.id.branchLayout);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        for (Course course : courses) {

            List<Branch> branches = databaseHandler.getBranchesForACourse(course.getCourse_ID());

            for (Branch branch : branches) {
                CardView cardView = new CardView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(12, 12, 12, 12);
                cardView.setLayoutParams(params);

                cardView.setRadius(15);
                cardView.setCardElevation(6);

                TextView textViewBranchName = new TextView(this);
                textViewBranchName.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textViewBranchName.setText(branch.getBranch_name());
                textViewBranchName.setTextSize(16);
                textViewBranchName.setPadding(16, 16, 16, 16);

                cardView.addView(textViewBranchName);

                branchLayout.addView(cardView);
            }
        }
    }

}
