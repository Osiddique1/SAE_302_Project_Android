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

public class ChecklistActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ChecklistPrefs";
    private EditText commentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        commentEdit = findViewById(R.id.comment_edittext);

        // --- TA TOOLBAR RÉINTÉGRÉE ---
        Toolbar toolbar = findViewById(R.id.toolbar_checklist);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Configuration des groupes d'exclusion
        setupCheckboxGroup(R.id.check_cleanliness_ok, R.id.check_cleanliness_revoir);
        setupCheckboxGroup(R.id.check_cabling_ok, R.id.check_cabling_revoir);
        setupCheckboxGroup(R.id.check_power_ok, R.id.check_power_revoir);
        setupCheckboxGroup(R.id.check_panels_ok, R.id.check_panels_revoir);
        setupCheckboxGroup(R.id.check_cable_type_ok, R.id.check_cable_type_revoir);
        setupCheckboxGroup(R.id.check_docs_ok, R.id.check_docs_revoir);
        setupCheckboxGroup(R.id.check_grounding_ok, R.id.check_grounding_revoir);
        setupCheckboxGroup(R.id.check_outlets_ok, R.id.check_outlets_revoir);

        if (findViewById(R.id.btn_save) != null) {
            findViewById(R.id.btn_save).setOnClickListener(v -> saveToPhone());
        }

        if (findViewById(R.id.btn_reset) != null) {
            findViewById(R.id.btn_reset).setOnClickListener(v -> {
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
                recreate();
            });
        }
    }

    private void saveToPhone() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        // Sauvegarde précise pour le rapport
        saveItem(editor, "clean", R.id.check_cleanliness_ok, R.id.check_cleanliness_revoir);
        saveItem(editor, "cable", R.id.check_cabling_ok, R.id.check_cabling_revoir);
        saveItem(editor, "power", R.id.check_power_ok, R.id.check_power_revoir);
        saveItem(editor, "panels", R.id.check_panels_ok, R.id.check_panels_revoir);
        saveItem(editor, "type", R.id.check_cable_type_ok, R.id.check_cable_type_revoir);
        saveItem(editor, "docs", R.id.check_docs_ok, R.id.check_docs_revoir);
        saveItem(editor, "ground", R.id.check_grounding_ok, R.id.check_grounding_revoir);
        saveItem(editor, "outlets", R.id.check_outlets_ok, R.id.check_outlets_revoir);

        editor.putString("comment", commentEdit.getText().toString());
        editor.apply();
        Toast.makeText(this, "Sauvegardé !", Toast.LENGTH_SHORT).show();
    }

    private void saveItem(SharedPreferences.Editor editor, String key, int okId, int revId) {
        editor.putBoolean(key + "_ok", ((CheckBox)findViewById(okId)).isChecked());
        editor.putBoolean(key + "_rev", ((CheckBox)findViewById(revId)).isChecked());
    }

    private void setupCheckboxGroup(int okId, int revoirId) {
        CheckBox ok = findViewById(okId);
        CheckBox rev = findViewById(revoirId);
        if (ok != null && rev != null) {
            ok.setOnCheckedChangeListener((b, isChecked) -> { if (isChecked) rev.setChecked(false); });
            rev.setOnCheckedChangeListener((b, isChecked) -> { if (isChecked) ok.setChecked(false); });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}