package com.affc.ait;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Student;

public class Register extends AppCompatActivity {
    Database_OLDCODE dbHelper;
    String[] genders = {"Male", "Female", "Other"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        setContentView(R.layout.activity_register);
        dbHelper = new Database_OLDCODE(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.gender);
        spinner.setAdapter(adapter);
    }

    public void redirectToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void register() {
        if (validateFields()) {
            //fields
            String name = findViewById(R.id.name).toString();
            String email = findViewById(R.id.email).toString();
            String address = findViewById(R.id.address).toString();
            String city = findViewById(R.id.city).toString();
            String phone = findViewById(R.id.phone).toString();
            String gender = findViewById(R.id.gender).toString();
            String dob = findViewById(R.id.dob).toString();

            Student student = new Student(name, email, address, city,phone,gender,dob);


            DatabaseHandler dbhandler = new DatabaseHandler(this);

        }

    }

    private boolean validateFields() {


        return false;

    }

    private void showErrorMessage(String message) {

    }


}
