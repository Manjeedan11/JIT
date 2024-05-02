package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.affc.ait.db.DatabaseHandler;

public class AdminLogin extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        btnAdminLogin = findViewById(R.id.btn_adminLogin);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (isAdminExists(username, password)) {
                    Intent intent = new Intent(AdminLogin.this, AdminDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLogin.this, "Admin credentials are incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //insertAdmin();
    }

    private boolean isAdminExists(String adminEmail, String adminPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        return dbHandler.authAdmin(adminEmail, adminPassword);
    }

    private void insertAdmin() {
        DatabaseHandler dbHandler = new DatabaseHandler(this);

        String adminEmail = "admin332@jit.com";
        String adminPassword = "admin33";

        String hashedPassword = dbHandler.getPasswordHash(adminPassword);

        boolean isAdminAdded = dbHandler.addAdmin(adminEmail, hashedPassword);

        if (isAdminAdded) {
            Toast.makeText(this, "Admin inserted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Admin already exists!", Toast.LENGTH_SHORT).show();
        }
    }

}
