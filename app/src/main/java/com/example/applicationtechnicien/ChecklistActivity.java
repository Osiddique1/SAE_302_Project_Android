package com.example.applicationtechnicien;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChecklistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comment out the content view and toolbar for one second
        // setContentView(R.layout.activity_checklist);

        // Add a simple message to see if the activity stays open
        android.widget.TextView tv = new android.widget.TextView(this);
        tv.setText("If you see this, the problem is in the XML layout.");
        setContentView(tv);


        // 2. Initialize Checkbox Groups safely
        setupCheckboxGroup(R.id.check_cleanliness_ok, R.id.check_cleanliness_revoir);
        setupCheckboxGroup(R.id.check_cabling_ok, R.id.check_cabling_revoir);
        setupCheckboxGroup(R.id.check_power_ok, R.id.check_power_revoir);
        setupCheckboxGroup(R.id.check_panels_ok, R.id.check_panels_revoir);
        setupCheckboxGroup(R.id.check_cable_type_ok, R.id.check_cable_type_revoir);
        setupCheckboxGroup(R.id.check_docs_ok, R.id.check_docs_revoir);
        setupCheckboxGroup(R.id.check_grounding_ok, R.id.check_grounding_revoir);
        setupCheckboxGroup(R.id.check_outlets_ok, R.id.check_outlets_revoir);
    }

    private void setupCheckboxGroup(int okId, int revoirId) {
        CheckBox okCheckBox = findViewById(okId);
        CheckBox revoirCheckBox = findViewById(revoirId);

        // CRASH PREVENTION: If the ID is wrong in XML, this stops the crash
        if (okCheckBox == null || revoirCheckBox == null) {
            return;
        }

        okCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) revoirCheckBox.setChecked(false);
        });

        revoirCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) okCheckBox.setChecked(false);
        });
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