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

/**
 * Activité de connexion / démarrage.
 * Permet de saisir l'identité du technicien et le nom du projet avant d'accéder au menu.
 */
public class MainActivity extends AppCompatActivity {

    // Déclaration des composants de l'interface utilisateur
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText projectTitleEditText;
    private Button loginButton;

    // Définition des clés constantes pour le transfert de données entre activités.
    // Utiliser le nom du package garantit que la clé est unique dans tout le système Android.
    public static final String EXTRA_FIRST_NAME = "com.example.applicationtechnicien.FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "com.example.applicationtechnicien.LAST_NAME";
    public static final String EXTRA_PROJECT_TITLE = "com.example.applicationtechnicien.PROJECT_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Active l'affichage immersif (Edge-to-Edge)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gestion automatique des marges (Padding) pour éviter que le contenu ne soit caché par
        // la barre d'état ou la barre de navigation du téléphone.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. INITIALISATION DES VUES
        // On lie les objets Java aux éléments définis dans le fichier XML (layout)
        firstNameEditText = findViewById(R.id.edit_text_first_name);
        lastNameEditText = findViewById(R.id.edit_text_last_name);
        projectTitleEditText = findViewById(R.id.edit_text_project_title);
        loginButton = findViewById(R.id.button_login);

        // 2. ÉCOUTEUR DE CLIC
        // Définit l'action à exécuter quand l'utilisateur appuie sur le bouton "Login"
        loginButton.setOnClickListener(v -> handleLogin());
    }

    /**
     * Logique de validation et de transition vers l'écran d'accueil.
     */
    private void handleLogin() {
        // Récupération des saisies utilisateur et suppression des espaces inutiles (trim)
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String projectTitle = projectTitleEditText.getText().toString().trim();

        // VALIDATION : On vérifie que tous les champs obligatoires sont remplis
        if (firstName.isEmpty() || lastName.isEmpty() || projectTitle.isEmpty()) {
            // Affiche un petit message d'erreur éphémère à l'écran
            Toast.makeText(this, "Veuillez entrer votre nom, prénom et le titre du projet.", Toast.LENGTH_SHORT).show();
            return; // On arrête la fonction ici si un champ est vide
        }

        // 3. PRÉPARATION DU CHANGEMENT D'ÉCRAN
        // L'Intent exprime l'intention de passer de cette activité à HomeActivity
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);

        // 4. TRANSFERT DES DONNÉES
        // On "attache" les informations saisies à l'Intent pour qu'elles soient récupérables plus tard
        intent.putExtra(EXTRA_FIRST_NAME, firstName);
        intent.putExtra(EXTRA_LAST_NAME, lastName);
        intent.putExtra(EXTRA_PROJECT_TITLE, projectTitle);

        // 5. DÉMARRAGE DE L'ACTIVITÉ SUIVANTE
        startActivity(intent);
    }
}