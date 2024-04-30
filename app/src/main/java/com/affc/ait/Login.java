package com.affc.ait;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    Database db;
    Button btnLogin;
    EditText etUsername, etPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        db = new Database(this);
//        etUsername = findViewById(R.id.setUsername); //?????
//        etPwd = findViewById(R.id.setPassword); //????? what
//        btnLogin = findViewById((R.id.btnLogin));
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                  boolean isLoggedId = db.checkUser(etUsername.getText().toString(), etPwd.getText().toString());
//                  if(isLoggedId){
//                      Intent intent = new Intent(Login.this, Home.class);
//                      startActivity(intent);
//                  }
//
//                  else {
//                      Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                  }
//            }
//        });
    }
}
