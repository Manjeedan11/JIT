package com.affc.ait;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.Properties;

public class Login extends AppCompatActivity {
    Database_OLDCODE db;
    Button btnLogin;

    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void handleSignInIntent(Intent intent) {
        Log.d(TAG, "handleSignInIntent:" + intent);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailLink = intent.getData().toString();

// Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                AuthResult result = task.getResult();
                                // You can access the new user via result.getUser()
                                // Additional user info profile *not* available via:
                                // result.getAdditionalUserInfo().getProfile() == null
                                // You can check if the user is new or existing:
                                // result.getAdditionalUserInfo().isNewUser()
                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                            }
                        }
                    });
        }
    }


    public void sendLoginCode(View view){
        EditText emailField = findViewById(R.id.email);
        email = emailField.getText().toString();
        if(email.isEmpty()){
            showErrorMessage("Email is required");
            return;
        }

        DatabaseHandler dbHandler = new DatabaseHandler(this);
        int id = dbHandler.verifyEmailExists(email);
        Log.e("id", "Email: " + email + " id: " + id);
        if (id != -1){
            Student student = new Student(id);
//            String code = dbHandler.updateLoginCode(student);

            ActionCodeSettings actionCodeSettings =
                    ActionCodeSettings.newBuilder()
                            // URL you want to redirect back to. The domain (www.example.com) for this
                            // URL must be whitelisted in the Firebase Console.
                            .setUrl("https://jit-mad.firebaseapp.com/")
                            // This must be true
                            .setHandleCodeInApp(true)
                            .setIOSBundleId("com.affc.ait")
                            .setAndroidPackageName(
                                    "com.affc.ait",
                                    true, /* installIfNotAvailable */
                                    "12"    /* minimumVersion */)
                            .setDynamicLinkDomain("affc.page.link")
                            .build();
            Log.d(TAG, "action code settings built");

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendSignInLinkToEmail(email, actionCodeSettings)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent to " + email);
                            }
                            Log.d(TAG, "inside onComplete");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error sending email", e);
                    });


        } else {
            showErrorMessage("Email not found");
        }
    }




    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

}
