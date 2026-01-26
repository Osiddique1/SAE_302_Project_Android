package com.example.applicationtechnicien;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class terrain extends AppCompatActivity {

    // On utilise le même nom de fichier ou un nouveau, selon votre choix
    // Ici, j'utilise "TerrainPrefs" pour ne pas mélanger avec la Checklist
    private static final String PREFS_NAME = "TerrainPrefs";

    private TextInputEditText etLieu, etAdresse, etCommentaire;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terrain);

        // --- 1. Gestion des Insets (Barres système) ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- 2. Initialisation de la Toolbar ---
        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // --- 3. Initialisation des composants ---
        etLieu = findViewById(R.id.etLieu);
        etAdresse = findViewById(R.id.etAdresse);
        etCommentaire = findViewById(R.id.etCommentaire);
        btnSave = findViewById(R.id.btnNouveauRdv); // C'est votre bouton "Prendre RDV"

        // On active le bouton pour qu'il puisse servir de sauvegarde
        btnSave.setEnabled(true);

        // --- 4. Charger les données existantes ---
        loadTerrainData();

        // --- 5. Sauvegarder lors du clic ---
        btnSave.setOnClickListener(v -> {
            saveTerrainData();
        });
    }

    private void saveTerrainData() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("lieu", etLieu.getText().toString());
        editor.putString("adresse", etAdresse.getText().toString());
        editor.putString("commentaire", etCommentaire.getText().toString());

        editor.apply(); // Sauvegarde asynchrone
        Toast.makeText(this, "Données du terrain sauvegardées !", Toast.LENGTH_SHORT).show();
    }

    private void loadTerrainData() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        etLieu.setText(sharedPref.getString("lieu", ""));
        etAdresse.setText(sharedPref.getString("adresse", ""));
        etCommentaire.setText(sharedPref.getString("commentaire", ""));
    }

    // Gestion du bouton retour de la Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}