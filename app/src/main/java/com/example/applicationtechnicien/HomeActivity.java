package com.example.applicationtechnicien;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
// Unused imports removed for clarity
// import androidx.core.graphics.Insets;
// import androidx.core.view.ViewCompat;
// import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This should be called before setContentView. It automatically handles system bar insets.
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_home);

        // The ViewCompat.setOnApplyWindowInsetsListener block is now redundant
        // because EdgeToEdge.enable(this) handles it. It's safe to remove it.
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        */

        // --- Correct Toolbar and ActionBar Setup ---
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        // Check if the ActionBar is not null before using it
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Optionally hide default title
        }

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Extract the first and last name from the Intent extras
        String firstName = intent.getStringExtra(MainActivity.EXTRA_FIRST_NAME);
        String lastName = intent.getStringExtra(MainActivity.EXTRA_LAST_NAME);

        // Find the TextView for the user's name
        TextView userNameTextView = findViewById(R.id.text_welcome);

        // Set the user's name in the TextView
        if (firstName != null && lastName != null) {
            userNameTextView.setText(firstName + " " + lastName);
        }
    }

    // This method handles clicks on action bar items, including the back button (home)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // android.R.id.home is the ID for the back button in the ActionBar
        if (item.getItemId() == android.R.id.home) {
            // This will simulate the user pressing the physical back button
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
