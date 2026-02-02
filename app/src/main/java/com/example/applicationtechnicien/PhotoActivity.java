package com.example.applicationtechnicien;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Activité dédiée à la prise de photos de chantier.
 * Elle gère l'accès à la caméra, la sauvegarde en galerie et l'affichage de l'aperçu.
 */
public class PhotoActivity extends AppCompatActivity {

    // Identifiants uniques pour les requêtes de permission et les résultats d'activité
    private static final int CAMERA_PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private ImageView imageView; // Vue pour afficher l'aperçu de la photo prise
    private Uri imageUri;         // Lien (URI) vers le fichier image créé

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // --- 1. CONFIGURATION DE LA TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbar_photo);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // Activation de la flèche de retour vers le menu Home
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        imageView = findViewById(R.id.image_view_photo);
        CardView btnCamera = findViewById(R.id.card_take_photo);

        // --- 2. GESTION DU CLIC ET DES PERMISSIONS ---
        btnCamera.setOnClickListener(v -> {
            // Pour Android 6.0 (Marshmallow) et plus, il faut demander la permission explicitement
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    // Demande la permission à l'utilisateur via une boîte de dialogue système
                    String[] permission = {Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(this, permission, CAMERA_PERMISSION_CODE);
                } else {
                    // Permission déjà accordée, on ouvre la caméra
                    openCamera();
                }
            } else {
                // Système ancien (inférieur à Marshmallow), l'accès est automatique au clic
                openCamera();
            }
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

    /**
     * Prépare le stockage et lance l'application caméra du téléphone.
     */
    private void openCamera() {
        // Préparation des métadonnées de l'image (titre et description dans la galerie)
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nouvelle Photo");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Prise depuis l'application Technicien");

        // Crée un emplacement vide pour l'image dans la Galerie Android et récupère son URI
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Création de l'Intent système pour capturer une image
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // On indique à la caméra d'enregistrer le résultat à l'adresse de notre imageUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_CAPTURE_CODE);
    }

    /**
     * Analyse la réponse de l'utilisateur à la demande de permission.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'utilisateur a cliqué sur "Autoriser"
                openCamera();
            } else {
                // L'utilisateur a cliqué sur "Refuser"
                Toast.makeText(this, "Permission refusée...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Se déclenche quand l'application Caméra se ferme après une prise de vue.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Si l'utilisateur a bien pris une photo et validé
        if (resultCode == RESULT_OK) {
            // Affiche l'image dans l'ImageView de notre application
            imageView.setImageURI(imageUri);
            Toast.makeText(this, "Photo enregistrée dans la Galerie !", Toast.LENGTH_SHORT).show();
        }
    }
}