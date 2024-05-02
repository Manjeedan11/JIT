package com.affc.ait;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Course;
import com.affc.ait.models.Student;

import java.util.List;

public class SearchUsers extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        //to get all students and put into view
        List<Student> students = databaseHandler.fetchStudents();
        EditText searchbar = findViewById(R.id.searchUser);
        if (students.size() == 0){
            Toast.makeText( this,"No Users Found", Toast.LENGTH_SHORT).show();
        }
        else {
            renderStudents(students);
        }




        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String query = searchbar.getText().toString();
                List<Student> searchResults = runSearch(query);
            }
        });


    }

    private List<Student> runSearch(String query) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        return databaseHandler.queryUsersByName(query);
    }

    private void renderStudents(List<Student> students) {
        // clear any rendered students
        LinearLayout linearLayout = findViewById(R.id.linearlayout); //Check ID with Isuru
        linearLayout.removeAllViews();

        for (Student student: students){
            // create a new Cardview
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(12,12,12,12); //Add margin to card view
            cardView.setLayoutParams(params);

            //Set card corner radius and elevation
            cardView.setRadius(12);
            cardView.setCardElevation(6);

            //create a LinearLayout for CardView context
            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));

            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setPadding(16,16,16,16); // add padding to the content

            //Create TextView for User name
            TextView textViewUserName = new TextView(this);
            textViewUserName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            textViewUserName.setText(student.getName());
            textViewUserName.setTextSize(20);
            textViewUserName.setTextColor(ContextCompat.getColor(this,R.color.black));

            // Add Textview to inner layout
            innerLayout.addView(textViewUserName);

            // Add inner layout to CardView
            cardView.addView(innerLayout);

            // Add CardView to the linearLayout
            linearLayout.addView(cardView);
        }
    }
}
