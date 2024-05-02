package com.affc.ait;


import android.Manifest;
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
import android.view.View;

import com.affc.ait.db.DatabaseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SetupProfileActivity extends AppCompatActivity {

    private long id;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;
    private static final int PERMISSION_REQUEST_CODE = 200;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                saveImageToStorage(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                saveImageToStorage(selectedImageUri);
            }
        }
    }

    private void saveImageToStorage(Object imageData) {
        if (imageData != null) {
            OutputStream outputStream;
            String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);

            if (imageData instanceof Bitmap) {
                Bitmap imageBitmap = (Bitmap) imageData;
                File imageFile = new File(storageDir, imageFileName);
                try {
                    outputStream = new FileOutputStream(imageFile);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    // Save image path to database
                    saveImagePathToDatabase(imageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (imageData instanceof Uri) {
                Uri selectedImageUri = (Uri) imageData;
                File imageFile = new File(storageDir, imageFileName);
                try {
                    outputStream = new FileOutputStream(imageFile);
                    outputStream.write(getBytesFromUri(selectedImageUri));
                    outputStream.close();
                    // Save image path to database
                    saveImagePathToDatabase(imageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void saveImagePathToDatabase(String imagePath) {
        if(id == -1) {
            return;
        }
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.addProfilePicture((int) id, imagePath);
    }

    private byte[] getBytesFromUri(Uri uri) throws IOException {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        }
    }




}