package com.example.applicationtechnicien;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.Calendar;
import java.util.Locale;

/**
 * Activité de saisie des informations de terrain.
 * Gère la localisation, la planification (date/heure) et les observations techniques.
 */
public class terrain extends AppCompatActivity {

    // Composants de saisie (Material Design)
    private TextInputEditText etLieu, etAdresse, etDate, etHeure, etCommentaire;
    private CheckBox cbProbleme;
    private Button btnSave, btnReset;

    // Objet pour le stockage permanent des données sur le téléphone
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activation du mode plein écran (bord à bord)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terrain);

        // Ajustement dynamique des marges pour ne pas chevaucher les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- CONFIGURATION DE LA TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialisation des éléments graphiques et des sélecteurs
        initViews();
        setupPickers();

        // Récupération du fichier de sauvegarde spécifique au "Terrain"
        prefs = getSharedPreferences("TerrainPrefs", Context.MODE_PRIVATE);

        // Chargement automatique des données précédemment enregistrées
        loadData();

        // Actions des boutons
        btnSave.setOnClickListener(v -> saveData());
        btnReset.setOnClickListener(v -> resetData());
    }

    /**
     * Liaison entre les objets Java et les IDs du fichier XML.
     */
    private void initViews() {
        etLieu = findViewById(R.id.etLieu);
        etAdresse = findViewById(R.id.etAdresse);
        etDate = findViewById(R.id.etDate);
        etHeure = findViewById(R.id.etHeure);
        etCommentaire = findViewById(R.id.etCommentaire);
        cbProbleme = findViewById(R.id.cbProbleme);
        btnSave = findViewById(R.id.btnNouveauRdv);
        btnReset = findViewById(R.id.btnResetTerrain);
    }

    /**
     * Configure les fenêtres surgissantes (Dialogs) pour choisir la date et l'heure.
     */
    private void setupPickers() {
        // Sélecteur de Date
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Formatage de la date en JJ-MM-AAAA
                        String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, (monthOfYear + 1), year1);
                        etDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Sélecteur d'Heure
        etHeure.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute1) -> {
                        // Formatage de l'heure en HH:MM
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        etHeure.setText(selectedTime);
                    }, hour, minute, true); // "true" pour le format 24h
            timePickerDialog.show();
        });
    }

    /**
     * Enregistre les informations saisies dans les SharedPreferences.
     */
    private void saveData() {
        prefs.edit()
                .putString("lieu", etLieu.getText().toString())
                .putString("adresse", etAdresse.getText().toString())
                .putString("date", etDate.getText().toString())
                .putString("heure", etHeure.getText().toString())
                .putString("commentaire", etCommentaire.getText().toString())
                .putBoolean("probleme", cbProbleme.isChecked())
                .apply(); // Sauvegarde asynchrone

        Toast.makeText(this, "Visite enregistrée avec succès !", Toast.LENGTH_SHORT).show();
    }

    /**
     * Remplit les champs avec les données stockées dans le téléphone.
     */
    private void loadData() {
        etLieu.setText(prefs.getString("lieu", ""));
        etAdresse.setText(prefs.getString("adresse", ""));
        etDate.setText(prefs.getString("date", ""));
        etHeure.setText(prefs.getString("heure", ""));
        etCommentaire.setText(prefs.getString("commentaire", ""));
        cbProbleme.setChecked(prefs.getBoolean("probleme", false));
    }

    /**
     * Efface les champs de l'écran et vide le fichier de sauvegarde.
     */
    private void resetData() {
        etLieu.setText("");
        etAdresse.setText("");
        etDate.setText("");
        etHeure.setText("");
        etCommentaire.setText("");
        cbProbleme.setChecked(false);

        prefs.edit().clear().apply(); // Vide physiquement le fichier TerrainPrefs
        Toast.makeText(this, "Données réinitialisées", Toast.LENGTH_SHORT).show();
    }

    /**
     * Gère l'action du bouton retour dans la barre d'outils.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme cette activité et retourne à l'écran précédent
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}