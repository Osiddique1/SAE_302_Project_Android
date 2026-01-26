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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        setupPickers(); // Logic for Date and Time

        prefs = getSharedPreferences("TerrainPrefs", Context.MODE_PRIVATE);
        loadData();

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

    private void setupPickers() {
        // Date Picker logic for dd-mm-yyyy
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Format: dd-mm-yyyy
                        String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, (monthOfYear + 1), year1);
                        etDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker logic
        etHeure.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute1) -> {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        etHeure.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}