package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfirmCourse extends AppCompatActivity {

    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_course);

        Intent intent = getIntent();
    }

    public void performEnroll(View view) {

    }

    public void sendEmail(){

    }


}