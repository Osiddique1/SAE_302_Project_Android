package com.example.applicationtechnicien;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhotoActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private ImageView imageViewPhoto;
    private Uri imageUri; // This will hold the URI for the saved image

    // This launcher will start the camera and wait for its result.
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // The photo was successfully saved to the imageUri.
                    // Now, we just display it in our ImageView.
                    imageViewPhoto.setImageURI(imageUri);
                    Toast.makeText(this, "Photo saved to Gallery!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the user cancels the camera
                    Toast.makeText(this, "Photo capture cancelled.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageViewPhoto = findViewById(R.id.image_view_photo);
        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.photos_title);
        }

        CardView takePictureButton = findViewById(R.id.card_take_photo);
        takePictureButton.setOnClickListener(v -> checkCameraPermissionAndOpenCamera());
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed to open the camera.
            openCamera();
        } else {
            // Permission is not granted, request it.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void openCamera() {
        // First, create a URI where the camera will save the photo.
        imageUri = createImageUri();
        if (imageUri == null) {
            Toast.makeText(this, "Failed to create image file.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an intent to open the camera.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // IMPORTANT: Tell the camera where to save the full-resolution photo.
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        // Launch the camera.
        cameraLauncher.launch(takePictureIntent);
    }

    // This method creates an entry in the public gallery and returns its URI.
    private Uri createImageUri() {
        ContentValues values = new ContentValues();
        // Create a unique file name using the current time
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Tell the system to save it in the public "Pictures" directory.
        // On modern Android (10+), this also puts it in a sub-folder for your app.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ApplicationTechnicien");
        }

        // Use ContentResolver to insert the new image entry and get its URI.
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted permission, now open the camera.
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
