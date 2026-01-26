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

        // 1. Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra(MainActivity.EXTRA_FIRST_NAME);
        String lastName = intent.getStringExtra(MainActivity.EXTRA_LAST_NAME);
        String projectTitle = intent.getStringExtra(MainActivity.EXTRA_PROJECT_TITLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (projectTitle != null && !projectTitle.isEmpty()) {
                getSupportActionBar().setTitle(projectTitle);
            } else {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        // 2. Welcome Message
        TextView userNameTextView = findViewById(R.id.text_welcome);
        if (firstName != null && lastName != null) {
            String welcomeMessage = getString(R.string.welcome_message, firstName, lastName);
            userNameTextView.setText(welcomeMessage);
        }

        // --- BUTTON 1: TERRAIN ---
        CardView terrainButton = findViewById(R.id.card_button_1);
        terrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fixed: Make sure both names are photoIntent
                Intent terrainIntent = new Intent(HomeActivity.this, terrain.class);
                startActivity(terrainIntent);
            }
        });

        // --- BUTTON 2: CHECKLIST ---
        CardView checklistButton = findViewById(R.id.card_button_2);
        checklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checklistIntent = new Intent(HomeActivity.this, ChecklistActivity.class);
                startActivity(checklistIntent);
            }
        });

        // --- BUTTON 3: PHOTOS ---
        CardView photosButton = findViewById(R.id.card_button_3);
        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(HomeActivity.this, PhotoActivity.class);
                startActivity(photoIntent);
            }
        });

        // --- BUTTON 4: Rapport ---
        CardView rapportButton = findViewById(R.id.card_button_3);
        rapportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rapportIntent = new Intent(HomeActivity.this, rapportActivity.class);
                startActivity(rapportIntent);
            }
        });

    } // <--- ALL buttons must be ABOVE this bracket

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}