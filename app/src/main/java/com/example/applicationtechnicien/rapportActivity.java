package com.example.applicationtechnicien;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Activité finale qui compile toutes les données saisies pour générer un rapport PDF.
 * Elle permet de sélectionner une photo d'illustration et fusionne les SharedPreferences.
 */
public class rapportActivity extends AppCompatActivity {

    private Uri selectedImageUri; // Adresse de l'image sélectionnée dans la galerie
    private ImageView imgPreview;
    private int currentY; // Variable de suivi de la position verticale pour l'écriture dans le PDF

    // Matrice contenant les clés de stockage et leurs labels lisibles pour le rapport
    private final String[][] reportItems = {
            {"clean", "Propreté Armoire"},
            {"cable", "Câblage"},
            {"power", "Électricité"},
            {"panels", "Étiquetage Panneaux"},
            {"type", "Type de Câble"},
            {"docs", "Documentation"},
            {"ground", "Mise à la terre"},
            {"outlets", "Prises Électriques"},
            {"security", "Sécurité Salle"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        // --- CONFIGURATION DE LA TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbar_rapport);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imgPreview = findViewById(R.id.img_preview);

        // --- BOUTON : SÉLECTION DE LA PHOTO DANS LA GALERIE ---
        findViewById(R.id.btn_select_photo).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        // --- BOUTON : DÉCLENCHEMENT DE LA GÉNÉRATION DU PDF ---
        findViewById(R.id.btn_generate_pdf).setOnClickListener(v -> generatePDF());
    }

    /**
     * Récupère l'image choisie par l'utilisateur et l'affiche en miniature.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgPreview.setImageURI(selectedImageUri);
        }
    }

    /**
     * Cœur du système : Crée un document PDF, dessine le texte et l'image dessus.
     */
    private void generatePDF() {
        // Accès aux deux fichiers de sauvegarde (Checklist et Terrain)
        SharedPreferences pCheck = getSharedPreferences("ChecklistPrefs", MODE_PRIVATE);
        SharedPreferences pTerrain = getSharedPreferences("TerrainPrefs", MODE_PRIVATE);

        // Création d'un nouveau document PDF
        PdfDocument document = new PdfDocument();
        // Définition de la page (Format A4 environ : 595x842 points)
        PdfDocument.Page page = document.startPage(new PdfDocument.PageInfo.Builder(595, 842, 1).create());

        Canvas canvas = page.getCanvas(); // Le "pinceau" pour dessiner sur la page
        Paint paint = new Paint();        // Les réglages du pinceau (taille, couleur, gras)
        currentY = 60;                    // Marge haute initiale

        // --- DESSIN DU TITRE ---
        paint.setTextSize(22f);
        paint.setFakeBoldText(true);
        canvas.drawText("RAPPORT D'INTERVENTION", 150, currentY, paint);
        currentY += 60;

        // --- DESSIN DES INFOS TERRAIN ---
        paint.setTextSize(14f);
        paint.setFakeBoldText(true);
        canvas.drawText("INFORMATIONS DU TERRAIN", 50, currentY, paint);
        paint.setFakeBoldText(false);
        currentY += 25;

        // Extraction des valeurs avec des valeurs par défaut "N/A" si vide
        String lieu = pTerrain.getString("lieu", "N/A");
        String adresse = pTerrain.getString("adresse", "N/A");
        String date = pTerrain.getString("date", "N/A");
        String heure = pTerrain.getString("heure", "N/A");
        boolean aProbleme = pTerrain.getBoolean("probleme", false);
        String commTerrain = pTerrain.getString("commentaire", "Aucun");

        // Écriture des données terrain sur le PDF
        canvas.drawText("Lieu : " + lieu, 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Adresse : " + adresse, 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Date : " + date + " à " + heure, 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Problème signalé : " + (aProbleme ? "OUI" : "NON"), 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Note terrain : " + commTerrain, 50, currentY, paint);

        currentY += 30;

        // --- DESSIN DE LA SECTION CHECKLIST ---
        paint.setFakeBoldText(true);
        canvas.drawText("Détails Checklist :", 50, currentY, paint);
        paint.setFakeBoldText(false);
        currentY += 25;

        // Boucle sur chaque élément de la checklist défini dans le tableau reportItems
        for (String[] item : reportItems) {
            boolean isOk = pCheck.getBoolean(item[0] + "_ok", false);
            boolean isRev = pCheck.getBoolean(item[0] + "_rev", false);

            String status = "NON REMPLI";
            if (isOk) status = "OK";
            else if (isRev) status = "À REVOIR";

            canvas.drawText("- " + item[1] + " : " + status, 70, currentY, paint);
            currentY += 25;
        }

        currentY += 20;
        canvas.drawText("Commentaire : " + pCheck.getString("comment", "Aucun"), 70, currentY, paint);

        // --- INSERTION DE LA PHOTO ---
        if (selectedImageUri != null) {
            currentY += 40;
            try {
                // Conversion de l'URI en Bitmap (image numérique)
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // Redimensionnement pour s'assurer que la photo ne dépasse pas de la page
                Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 300, 225, false);
                canvas.drawBitmap(scaledBmp, 140, currentY, paint);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors de l'ajout de la photo", Toast.LENGTH_SHORT).show();
            }
        }

        // Finalisation de la page
        document.finishPage(page);
        // Appel de la méthode de sauvegarde sur le disque
        savePDF(document);
    }

    /**
     * Enregistre physiquement le document dans le dossier "Documents" du téléphone.
     */
    private void savePDF(PdfDocument doc) {
        // Création du nom de fichier unique basé sur l'heure actuelle
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Rapport_" + System.currentTimeMillis() + ".pdf");
        try {
            // Écriture réelle des données dans le fichier
            doc.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF créé dans le dossier Documents", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
        }
        // Libération de la mémoire
        doc.close();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}