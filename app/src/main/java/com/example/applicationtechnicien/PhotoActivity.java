package com.example.applicationtechnicien;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class PhotoActivity extends AppCompatActivity {

    // Request code to identify the camera intent result
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageViewPhoto; // Declaration for the ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo);

        // --- View Initialization ---
        imageViewPhoto = findViewById(R.id.image_view_photo); // Initialize ImageView

        // --- Toolbar Setup ---
        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.photos_title);
        }

        // --- Button Click Listener (Take Photo) ---
        CardView takePictureButton = findViewById(R.id.card_take_photo);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    // Opens the device's camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there is an app that can handle this intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Camera not available.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the result returned from the camera activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from our camera request and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            // Get the thumbnail bitmap provided by the camera intent
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Set the bitmap to the ImageView
            imageViewPhoto.setImageBitmap(imageBitmap);

            // NOTE: This approach only provides a small thumbnail.
            // For a full-resolution photo saved to the phone, you would need
            // to specify a file path using a FileProvider before starting the camera intent.

            Toast.makeText(this, "Photo captured successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handles the back button in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}