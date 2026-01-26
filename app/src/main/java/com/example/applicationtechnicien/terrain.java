package com.example.applicationtechnicien;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
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

    private TextInputEditText etLieu, etAdresse, etDate, etHeure, etCommentaire;
    private CheckBox cbProbleme;
    private Button btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terrain);

        // 1. Gestion de l'affichage (Barres système)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Toolbar (Fonctionne maintenant avec le thème NoActionBar)
        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 3. Initialisation des vues
        initViews();

        // 4. Chargement des données sauvegardées
        prefs = getSharedPreferences("TerrainPrefs", Context.MODE_PRIVATE);
        loadData();

        // 5. Activation et clic du bouton
        btnSave.setEnabled(true);
        btnSave.setOnClickListener(v -> saveData());
    }

    private void initViews() {
        etLieu = findViewById(R.id.etLieu);
        etAdresse = findViewById(R.id.etAdresse);
        etDate = findViewById(R.id.etDate);
        etHeure = findViewById(R.id.etHeure);
        etCommentaire = findViewById(R.id.etCommentaire);
        cbProbleme = findViewById(R.id.cbProbleme);
        btnSave = findViewById(R.id.btnNouveauRdv);
    }

    private void saveData() {
        prefs.edit()
                .putString("lieu", etLieu.getText().toString())
                .putString("adresse", etAdresse.getText().toString())
                .putString("date", etDate.getText().toString())
                .putString("heure", etHeure.getText().toString())
                .putString("commentaire", etCommentaire.getText().toString())
                .putBoolean("probleme", cbProbleme.isChecked())
                .apply();

        Toast.makeText(this, "Visite enregistrée avec succès !", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        etLieu.setText(prefs.getString("lieu", ""));
        etAdresse.setText(prefs.getString("adresse", ""));
        etDate.setText(prefs.getString("date", ""));
        etHeure.setText(prefs.getString("heure", ""));
        etCommentaire.setText(prefs.getString("commentaire", ""));
        cbProbleme.setChecked(prefs.getBoolean("probleme", false));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme l'activité et retourne au menu
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}