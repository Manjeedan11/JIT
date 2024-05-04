package com.affc.ait;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Course;
import com.affc.ait.models.Student;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchUsers extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        // Get reference to the search bar
        EditText searchbar = findViewById(R.id.searchUser);

        // Fetch all students from the database
        List<Student> students = databaseHandler.fetchStudents();

        // Check if students are available
        if (students.size() == 0) {
            Toast.makeText(this, "No Users Found", Toast.LENGTH_SHORT).show();
        } else {
            // Render the students
            renderStudents(students);
        }

        // Add text watcher to the search bar for live search functionality
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Perform search based on the entered query
                String query = searchbar.getText().toString();
                List<Student> searchResults = runSearch(query);
                // Render search results
                renderStudents(searchResults);
            }
        });
    }

    // Method to perform search based on the query
    private List<Student> runSearch(String query) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        return databaseHandler.queryUsersByName(query);
    }

    // Method to render the list of students
    private void renderStudents(List<Student> students) {
        // Clear any previously rendered students
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        // Loop through each student and render them
        for (Student student : students) {
            // Create a new CardView
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(12, 12, 12, 12);
            cardView.setLayoutParams(layoutParams);

            // Create a new RelativeLayout for the content inside the CardView
            RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.dark_blue));

            // Create ImageView for the student's image
            ImageView imageView = new ImageView(this);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                    100,
                    100
            );
            imageView.setLayoutParams(imageParams);
            imageView.setId(View.generateViewId()); // Generate unique ID for each view
            // Load the student's image using Picasso library or default image
            if (student.getProfilePicture() != null && !student.getProfilePicture().isEmpty()) {
                String img_path = student.getProfilePicture();
                Log.d("img pth", img_path);
                Picasso.get().load(img_path).error(R.drawable.user).into(imageView);
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user));
            }

            // Add ImageView to RelativeLayout
            relativeLayout.addView(imageView);

            // Create TextViews for student information (Name, Email, Phone)
            TextView nameTextView = createTextView(student.getName(), 80, 100);
            TextView emailTextView = createTextView(student.getEmail(), 80, 150);
            TextView phoneTextView = createTextView(student.getPhone(), 80, 200);

            // Add TextViews to RelativeLayout
            relativeLayout.addView(nameTextView);
            relativeLayout.addView(emailTextView);
            relativeLayout.addView(phoneTextView);

            // Add RelativeLayout to CardView
            cardView.addView(relativeLayout);

            // Add CardView to LinearLayout
            linearLayout.addView(cardView);
        }
    }

    // Method to create TextView dynamically
    private TextView createTextView(String text, int marginLeft, int marginTop) {
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginLeft, marginTop, 0, 0);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(18);
        return textView;
    }

    @Override
    protected  void onResume(){
        super.onResume();
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        List<Student> students = databaseHandler.fetchStudents();
        renderStudents(students);
    }
}
