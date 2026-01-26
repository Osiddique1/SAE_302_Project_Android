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

    // Unique name for our "notebook"
    private static final String PREFS_NAME = "ChecklistPrefs";
    private EditText commentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        commentEdit = findViewById(R.id.comment_edittext);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_checklist);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 2. Setup Checkbox Logic
        setupCheckboxGroup(R.id.check_cleanliness_ok, R.id.check_cleanliness_revoir);
        setupCheckboxGroup(R.id.check_cabling_ok, R.id.check_cabling_revoir);
        setupCheckboxGroup(R.id.check_power_ok, R.id.check_power_revoir);
        setupCheckboxGroup(R.id.check_panels_ok, R.id.check_panels_revoir);
        setupCheckboxGroup(R.id.check_cable_type_ok, R.id.check_cable_type_revoir);
        setupCheckboxGroup(R.id.check_docs_ok, R.id.check_docs_revoir);
        setupCheckboxGroup(R.id.check_grounding_ok, R.id.check_grounding_revoir);
        setupCheckboxGroup(R.id.check_outlets_ok, R.id.check_outlets_revoir);

        // 3. Load previously saved data (if any)
        loadSavedData();

        // 4. Save button logic
        // (Make sure you added android:id="@+id/btn_save" to your XML button!)
        if (findViewById(R.id.btn_save) != null) {
            findViewById(R.id.btn_save).setOnClickListener(v -> saveToPhone());
        }
    }

    private void saveToPhone() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Save Checkbox states (Checked or Not)
        editor.putBoolean("clean_ok", ((CheckBox)findViewById(R.id.check_cleanliness_ok)).isChecked());
        editor.putBoolean("clean_rev", ((CheckBox)findViewById(R.id.check_cleanliness_revoir)).isChecked());

        // Save Comment
        editor.putString("comment", commentEdit.getText().toString());

        editor.apply(); // This saves the data!
        Toast.makeText(this, "Sauvegardé !", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedData() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Restore Checkbox states
        ((CheckBox)findViewById(R.id.check_cleanliness_ok)).setChecked(sharedPref.getBoolean("clean_ok", false));
        ((CheckBox)findViewById(R.id.check_cleanliness_revoir)).setChecked(sharedPref.getBoolean("clean_rev", false));

        // Restore Comment
        commentEdit.setText(sharedPref.getString("comment", ""));
    }

    private void setupCheckboxGroup(int okId, int revoirId) {
        CheckBox ok = findViewById(okId);
        CheckBox rev = findViewById(revoirId);
        if (ok == null || rev == null) return;

        ok.setOnCheckedChangeListener((b, isChecked) -> { if (isChecked) rev.setChecked(false); });
        rev.setOnCheckedChangeListener((b, isChecked) -> { if (isChecked) ok.setChecked(false); });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}