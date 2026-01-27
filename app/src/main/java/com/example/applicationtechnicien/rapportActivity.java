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

public class rapportActivity extends AppCompatActivity {

    private Uri selectedImageUri; // C'est ici que l'image est stockée
    private ImageView imgPreview;
    private int currentY;

    // Liste des clés de la checklist (doit correspondre à ChecklistActivity)
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

        // --- TA TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.toolbar_rapport);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imgPreview = findViewById(R.id.img_preview);

        // --- BOUTON SÉLECTION PHOTO ---
        findViewById(R.id.btn_select_photo).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        // --- BOUTON GÉNÉRER PDF ---
        findViewById(R.id.btn_generate_pdf).setOnClickListener(v -> generatePDF());
    }

    // Capture le résultat de la galerie photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgPreview.setImageURI(selectedImageUri);
        }
    }

    private void generatePDF() {
        SharedPreferences pCheck = getSharedPreferences("ChecklistPrefs", MODE_PRIVATE);
        SharedPreferences pTerrain = getSharedPreferences("TerrainPrefs", MODE_PRIVATE);

        PdfDocument document = new PdfDocument();
        PdfDocument.Page page = document.startPage(new PdfDocument.PageInfo.Builder(595, 842, 1).create());
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        currentY = 60;

        // --- TITRE ---
        paint.setTextSize(22f); paint.setFakeBoldText(true);
        canvas.drawText("RAPPORT D'INTERVENTION", 150, currentY, paint);
        currentY += 60;

        // --- INFOS TERRAIN ---
        paint.setTextSize(14f);
        paint.setFakeBoldText(true);
        canvas.drawText("INFORMATIONS DU TERRAIN", 50, currentY, paint);
        paint.setFakeBoldText(false);
        currentY += 25;

        // Récupération de toutes les données de TerrainPrefs
        String lieu = pTerrain.getString("lieu", "N/A");
        String adresse = pTerrain.getString("adresse", "N/A");
        String date = pTerrain.getString("date", "N/A");
        String heure = pTerrain.getString("heure", "N/A");
        boolean aProbleme = pTerrain.getBoolean("probleme", false);
        String commTerrain = pTerrain.getString("commentaire", "Aucun");

        // Affichage ligne par ligne
        canvas.drawText("Lieu : " + lieu, 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Adresse : " + adresse, 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Date : " + date + " à " + heure, 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Problème signalé : " + (aProbleme ? "OUI" : "NON"), 50, currentY, paint);
        currentY += 20;
        canvas.drawText("Note terrain : " + commTerrain, 50, currentY, paint);

        currentY += 30; // Espace avant la section Checklist

        // --- SECTION CHECKLIST ---
        paint.setFakeBoldText(true);
        canvas.drawText("Détails Checklist :", 50, currentY, paint);
        paint.setFakeBoldText(false);
        currentY += 25;

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

        // --- SECTION PHOTO (VÉRIFICATION ET AFFICHAGE) ---
        if (selectedImageUri != null) {
            currentY += 40;
            try {
                // On transforme l'URI en Bitmap pour le PDF
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // On redimensionne l'image pour qu'elle tienne dans le PDF (Largeur 300, Hauteur 220)
                Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 300, 225, false);
                canvas.drawBitmap(scaledBmp, 140, currentY, paint);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors de l'ajout de la photo", Toast.LENGTH_SHORT).show();
            }
        }

        document.finishPage(page);
        savePDF(document);
    }

    private void savePDF(PdfDocument doc) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Rapport_" + System.currentTimeMillis() + ".pdf");
        try {
            doc.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF créé dans le dossier Documents", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
        }
        doc.close();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}