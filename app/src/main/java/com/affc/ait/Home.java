package com.affc.ait;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.affc.ait.db.DatabaseHandler;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        //get all courses and put into view

    }
}
