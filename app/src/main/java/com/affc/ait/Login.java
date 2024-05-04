package com.affc.ait;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Login extends AppCompatActivity {
    Button btnLogin;

    private String email;
    private String code;

    private UserInstance userInstance;
    private DatabaseHandler handler;
    EditText emailEditText, codeText;

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

    public void sendLoginCode(View view) {
        // Generate a random 6-digit code
        // Save the email to SharedPreferences
        emailEditText = findViewById(R.id.email);
        email = emailEditText.getText().toString().trim();
        int id = handler.verifyEmailExists(email);
        if(id == -1) {
            showErrorMessage("Email not found");
            return;
        }
        Student student = new Student(id);

        code = handler.updateLoginCode(student);
        if(code == null) {
            showErrorMessage("Failed to generate code");
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
        // Send the generated code to the email using AsyncTask
        new SendEmailTask().execute();
    }

    public void browseAsGuest(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


    // AsyncTask to send email in background
    class SendEmailTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            emailEditText = findViewById(R.id.email);
            String userEmail = emailEditText.getText().toString().trim();
            String stringSenderEmail = "isuruyahampath1@gmail.com";
            String stringReceiverEmail = email; // Retrieve email from SharedPreferences
            String stringPasswordSenderEmail = "efzn qwof iaea qgmw";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                mimeMessage.setSubject("Login Magic Code");
                mimeMessage.setText("Your code is " + code);

                Transport.send(mimeMessage);

                return true;

            } catch (AddressException e) {
                Log.e("E", "ERROR ADDRESS EXCEPTION");
                e.printStackTrace();
            } catch (MessagingException e) {
                Log.e("E", "ERROR MESSAGE EXCEPTION");
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(Login.this, "Code Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, "Failed to send code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to validate confirmation code
    public void verifyCode(View view) {
        codeText = findViewById(R.id.code);

        String enteredCode = codeText.getText().toString();
        Log.e(TAG, "verifyCode: " + enteredCode + " " + code);

        if(enteredCode.equals(code) || code.equals("123456")) { /*bypass email for testing*/
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("studentID", handler.verifyEmailExists(email));
            editor.apply();
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
        }
         else {
            // Confirmation code does not match
            Toast.makeText(this, "Invalid confirmation code", Toast.LENGTH_SHORT).show();
        }
    }

    public void redirect(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}
