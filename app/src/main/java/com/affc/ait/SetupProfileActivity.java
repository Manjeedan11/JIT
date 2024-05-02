package com.affc.ait;


import static android.content.ContentValues.TAG;

import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.affc.ait.db.DatabaseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.github.dhaval2404.imagepicker.ImagePicker;


public class SetupProfileActivity extends AppCompatActivity {

    private long id;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        Intent intent = getIntent();
        id = intent.getLongExtra("student_id", -1);
    }

    public void redirectToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openSelector(View view){
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    public void completeSelection(View view) {
        if (path == null) {
            showErrorMessage("Please select a profile picture");
            return;
        }
        Log.e(TAG, path);
        saveImagePathToDatabase(path);
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ImageView imageView = findViewById(R.id.profilePicture);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            assert data != null;
            Uri uri = data.getData();
            imageView.setBackground(null);
            path = uri.getPath();
            imageView.setImageURI(uri);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveImagePathToDatabase(String imagePath) {
        if(id == -1) {
            return;
        }
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.addProfilePicture((int) id, imagePath);
    }

}