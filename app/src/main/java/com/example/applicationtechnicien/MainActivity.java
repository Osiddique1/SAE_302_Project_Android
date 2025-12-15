package com.example.applicationtechnicien;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    // NOUVEAU: Référence pour le champ du titre du projet
    private EditText projectTitleEditText;
    private Button loginButton;

    // Keys for passing data to the next activity
    public static final String EXTRA_FIRST_NAME = "com.example.applicationtechnicien.FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "com.example.applicationtechnicien.LAST_NAME";

    // CLEF EXISTANTE: Clé pour le titre du projet
    public static final String EXTRA_PROJECT_TITLE = "com.example.applicationtechnicien.PROJECT_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize views
        firstNameEditText = findViewById(R.id.edit_text_first_name);
        lastNameEditText = findViewById(R.id.edit_text_last_name);
        // NOUVEAU: Initialisation du champ du titre du projet
        projectTitleEditText = findViewById(R.id.edit_text_project_title); // ASSUMER CET ID
        loginButton = findViewById(R.id.button_login);

        // 2. Set up the login button click listener
        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        // NOUVEAU: Récupérer le titre du projet
        String projectTitle = projectTitleEditText.getText().toString().trim();

        // Basic validation: ensure necessary fields are not empty (ajout du titre du projet à la validation)
        if (firstName.isEmpty() || lastName.isEmpty() || projectTitle.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre nom, prénom et le titre du projet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Create an Intent to navigate to the HomeActivity
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);

        // 4. Pass the entered data to the next Activity
        intent.putExtra(EXTRA_FIRST_NAME, firstName);
        intent.putExtra(EXTRA_LAST_NAME, lastName);
        // NOUVEAU: Passer le titre du projet
        intent.putExtra(EXTRA_PROJECT_TITLE, projectTitle);

        // 5. Start the new Activity
        startActivity(intent);
    }
}