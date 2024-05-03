package com.affc.ait;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Student;
import com.affc.ait.utils.EmailHandler;
import com.affc.ait.utils.UserInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Properties;

public class Login extends AppCompatActivity {
    Button btnLogin;

    private String email;

    private UserInstance userInstance;
    private DatabaseHandler handler;

    private EmailHandler emailer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        handler = new DatabaseHandler(this);
        userInstance = UserInstance.getInstance(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }


    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private void sendLoginCode(View view) {
        Student student = new Student(handler.verifyEmailExists(email));
        if (student.getId() == -1) {
            showErrorMessage("Email not found");
            return;
        }
        else {
            userInstance.setEmail(email);
            handler.updateLoginCode(student);
            String code = student.generateLoginCode();

            emailer = new EmailHandler(email, code, true);
            emailer.execute();

            Log.d(TAG, "login code sent");

        }
    }

    public void verifyCode(View view) {
        email = userInstance.getEmail();
        if (email.isEmpty()) {
            showErrorMessage("Email cannot be empty");
            return;
        }
        EditText codeField = findViewById(R.id.code);
        String code = codeField.getText().toString();
        if(code.isEmpty()) {
            showErrorMessage("Code cannot be empty");
        }

        if (handler.authStudent(email,code)) {
            userInstance.setUserId(handler.verifyEmailExists(email));
            userInstance.setAdmin(false);
            Intent intent = new Intent();
            intent.setClass(this, Home.class);
            startActivity(intent);
            finish();
        };

    }



    public void redirect(View view) {
        Intent intent = new Intent();
        intent.setClass(this, Register.class);
        startActivity(intent);
    }

}
