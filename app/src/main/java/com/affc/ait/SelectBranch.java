package com.affc.ait;

import android.content.Intent;
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
        Intent intent = getIntent();
        int courseID = intent.getIntExtra("course_ID", -1);
        List<Branch> branches = databaseHandler.getBranchesForACourse(courseID);

        renderBranchesForCourses(branches);
    }

    private void renderBranchesForCourses(List<Branch> branches) {
        LinearLayout branchLayout = findViewById(R.id.branchLayout);

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
