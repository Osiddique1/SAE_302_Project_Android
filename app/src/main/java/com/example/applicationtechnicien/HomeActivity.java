package com.example.applicationtechnicien;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // --- Correct Toolbar and ActionBar Setup ---
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Extract the data from the Intent extras
        String firstName = intent.getStringExtra(MainActivity.EXTRA_FIRST_NAME);
        String lastName = intent.getStringExtra(MainActivity.EXTRA_LAST_NAME);
        // NOUVEAU: Récupérer le titre du projet
        String projectTitle = intent.getStringExtra(MainActivity.EXTRA_PROJECT_TITLE);

        // --- DÉFINIR LE TITRE DE LA BARRE D'OUTILS AVEC LE TITRE DU PROJET ---
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button

            // Si le titre du projet existe, utilisez-le comme titre de la Toolbar
            if (projectTitle != null && !projectTitle.isEmpty()) {
                getSupportActionBar().setTitle(projectTitle); // Définir le titre
                getSupportActionBar().setDisplayShowTitleEnabled(true); // Afficher le titre
            } else {
                // Sinon, revenez à l'ancienne méthode (Dashboard, ou pas de titre)
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        // Find the TextView for the user's name
        TextView userNameTextView = findViewById(R.id.text_welcome);

        // Set the user's name in the TextView
        if (firstName != null && lastName != null) {
            String welcomeMessage = getString(R.string.welcome_message, firstName, lastName);
            userNameTextView.setText(welcomeMessage);
        }

        // ==============================================
        // 🚀 NEW CODE TO START PHOTOACTIVITY
        // ==============================================

        // 1. Find the CardView for 'Photos' (R.id.card_button_3)
        CardView photosButton = findViewById(R.id.card_button_3);

        // 2. Set the OnClickListener
        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3. Create an Intent to start PhotoActivity
                Intent photoIntent = new Intent(HomeActivity.this, PhotoActivity.class);
                // 4. Start the new activity
                startActivity(photoIntent);
            }
        });

        // ==============================================
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