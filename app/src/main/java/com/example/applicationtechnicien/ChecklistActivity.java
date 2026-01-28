package com.example.applicationtechnicien;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Activité gérant la checklist technique.
 * Permet de cocher des états (OK/À revoir), d'ajouter des commentaires et de sauvegarder localement.
 */
public class ChecklistActivity extends AppCompatActivity {

    // Nom du fichier de préférences pour le stockage local
    private static final String PREFS_NAME = "ChecklistPrefs";
    private EditText commentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Initialisation du champ de saisie des commentaires
        commentEdit = findViewById(R.id.comment_edittext);

        // --- CONFIGURATION DE LA TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbar_checklist);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // Activation du bouton "Retour" dans la barre d'outils
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // --- CONFIGURATION DES GROUPES D'EXCLUSION ---
        // On s'assure que cocher "OK" décoche "À revoir" et inversement pour chaque catégorie
        setupCheckboxGroup(R.id.check_cleanliness_ok, R.id.check_cleanliness_revoir);
        setupCheckboxGroup(R.id.check_cabling_ok, R.id.check_cabling_revoir);
        setupCheckboxGroup(R.id.check_power_ok, R.id.check_power_revoir);
        setupCheckboxGroup(R.id.check_panels_ok, R.id.check_panels_revoir);
        setupCheckboxGroup(R.id.check_cable_type_ok, R.id.check_cable_type_revoir);
        setupCheckboxGroup(R.id.check_docs_ok, R.id.check_docs_revoir);
        setupCheckboxGroup(R.id.check_grounding_ok, R.id.check_grounding_revoir);
        setupCheckboxGroup(R.id.check_outlets_ok, R.id.check_outlets_revoir);

        // --- GESTION DES BOUTONS D'ACTION ---
        // Bouton de sauvegarde
        if (findViewById(R.id.btn_save) != null) {
            findViewById(R.id.btn_save).setOnClickListener(v -> saveToPhone());
        }

        // Bouton de réinitialisation (efface les données et recharge l'écran)
        if (findViewById(R.id.btn_reset) != null) {
            findViewById(R.id.btn_reset).setOnClickListener(v -> {
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
                recreate(); // Relance l'activité pour remettre l'interface à zéro
            });
        }
    }

    /**
     * Récupère l'état de tous les éléments et les enregistre dans les SharedPreferences.
     */
    private void saveToPhone() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        // Sauvegarde de l'état (Boolean) pour chaque point de contrôle
        saveItem(editor, "clean", R.id.check_cleanliness_ok, R.id.check_cleanliness_revoir);
        saveItem(editor, "cable", R.id.check_cabling_ok, R.id.check_cabling_revoir);
        saveItem(editor, "power", R.id.check_power_ok, R.id.check_power_revoir);
        saveItem(editor, "panels", R.id.check_panels_ok, R.id.check_panels_revoir);
        saveItem(editor, "type", R.id.check_cable_type_ok, R.id.check_cable_type_revoir);
        saveItem(editor, "docs", R.id.check_docs_ok, R.id.check_docs_revoir);
        saveItem(editor, "ground", R.id.check_grounding_ok, R.id.check_grounding_revoir);
        saveItem(editor, "outlets", R.id.check_outlets_ok, R.id.check_outlets_revoir);

        // Sauvegarde du texte du commentaire
        editor.putString("comment", commentEdit.getText().toString());

        // Validation des modifications
        editor.apply();
        Toast.makeText(this, "Sauvegardé !", Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper pour sauvegarder les deux états d'une ligne de checklist.
     */
    private void saveItem(SharedPreferences.Editor editor, String key, int okId, int revId) {
        editor.putBoolean(key + "_ok", ((CheckBox)findViewById(okId)).isChecked());
        editor.putBoolean(key + "_rev", ((CheckBox)findViewById(revId)).isChecked());
    }

    /**
     * Crée une logique d'exclusion mutuelle entre deux CheckBox.
     * Si l'une est cochée, l'autre se décoche automatiquement.
     */
    private void setupCheckboxGroup(int okId, int revoirId) {
        CheckBox ok = findViewById(okId);
        CheckBox rev = findViewById(revoirId);
        if (ok != null && rev != null) {
            ok.setOnCheckedChangeListener((b, isChecked) -> { if (isChecked) rev.setChecked(false); });
            rev.setOnCheckedChangeListener((b, isChecked) -> { if (isChecked) ok.setChecked(false); });
        }
    }

    /**
     * Gère les clics sur les items du menu (notamment la flèche de retour).
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Si l'utilisateur appuie sur la flèche retour de la toolbar
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme l'activité actuelle
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}