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

/**
 * Menu principal de l'application.
 * Cette activité centralise l'accès aux différentes fonctionnalités (Terrain, Checklist, Photos, Rapport).
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Active l'affichage bord-à-bord (sous la barre de statut et la barre de navigation)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // 1. CONFIGURATION DE LA TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        // Récupération des données transmises par l'écran de connexion (MainActivity)
        Intent intent = getIntent();
        String firstName = intent.getStringExtra(MainActivity.EXTRA_FIRST_NAME);
        String lastName = intent.getStringExtra(MainActivity.EXTRA_LAST_NAME);
        String projectTitle = intent.getStringExtra(MainActivity.EXTRA_PROJECT_TITLE);

        // Configuration du titre et du bouton retour dans la barre d'outils
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Si un titre de projet a été saisi, on l'affiche, sinon on cache le titre
            if (projectTitle != null && !projectTitle.isEmpty()) {
                getSupportActionBar().setTitle(projectTitle);
            } else {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        // 2. MESSAGE DE BIENVENUE PERSONNALISÉ
        TextView userNameTextView = findViewById(R.id.text_welcome);
        if (firstName != null && lastName != null) {
            // Utilise une String ressource formatée pour assembler le message de bienvenue
            String welcomeMessage = getString(R.string.welcome_message, firstName, lastName);
            userNameTextView.setText(welcomeMessage);
        }

        // --- BOUTON 1 : ACCÈS AU FORMULAIRE TERRAIN ---
        CardView terrainButton = findViewById(R.id.card_button_1);
        terrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terrainIntent = new Intent(HomeActivity.this, terrain.class);
                startActivity(terrainIntent);
            }
        });

        // --- BOUTON 2 : ACCÈS À LA CHECKLIST TECHNIQUE ---
        CardView checklistButton = findViewById(R.id.card_button_2);
        checklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checklistIntent = new Intent(HomeActivity.this, ChecklistActivity.class);
                startActivity(checklistIntent);
            }
        });

        // --- BOUTON 3 : ACCÈS À LA PRISE DE PHOTOS ---
        CardView photosButton = findViewById(R.id.card_button_3);
        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(HomeActivity.this, PhotoActivity.class);
                startActivity(photoIntent);
            }
        });

        // --- BOUTON 4 : GÉNÉRATION OU CONSULTATION DU RAPPORT ---
        CardView rapportButton = findViewById(R.id.card_button_4);
        rapportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rapportIntent = new Intent(HomeActivity.this, rapportActivity.class);
                startActivity(rapportIntent);
            }
        });

    } // Fin de onCreate : Tous les branchements de boutons sont terminés ici

    /**
     * Gère les interactions avec le menu de la Toolbar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gère l'appui sur la flèche de retour (identifiée par android.R.id.home)
        if (item.getItemId() == android.R.id.home) {
            // Utilise le Dispatcher pour simuler l'appui sur le bouton retour physique/système
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}