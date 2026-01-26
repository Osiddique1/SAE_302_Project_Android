package com.example.applicationtechnicien;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class rapportActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        imgPreview = findViewById(R.id.img_preview);
        Button btnSelect = findViewById(R.id.btn_select_photo);
        Button btnPdf = findViewById(R.id.btn_generate_pdf);

        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnPdf.setOnClickListener(v -> generatePDF());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgPreview.setImageURI(selectedImageUri);
        }
    }

    private void generatePDF() {
        // 1. Récupération des données SharedPreferences
        SharedPreferences terrainPrefs = getSharedPreferences("TerrainPrefs", Context.MODE_PRIVATE);
        String lieu = terrainPrefs.getString("lieu", "Non précisé");
        String adresse = terrainPrefs.getString("adresse", "Non précisée");

        SharedPreferences checklistPrefs = getSharedPreferences("ChecklistPrefs", Context.MODE_PRIVATE);
        String comment = checklistPrefs.getString("comment", "");

        // 2. Création du Document PDF
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // --- Titre ---
        paint.setTextSize(22f);
        paint.setFakeBoldText(true);
        canvas.drawText("RAPPORT D'INTERVENTION", 150, 50, paint);

        // --- Texte des données ---
        paint.setFakeBoldText(false);
        paint.setTextSize(14f);
        canvas.drawText("Lieu : " + lieu, 50, 100, paint);
        canvas.drawText("Adresse : " + adresse, 50, 130, paint);
        canvas.drawText("Commentaire : " + comment, 50, 160, paint);

        // --- Ajout de la Photo ---
        if (selectedImageUri != null) {
            try {
                // Conversion de l'URI en Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Redimensionner l'image pour qu'elle tienne dans le PDF (ex: largeur 400px)
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 300, false);

                // Dessiner l'image sur le PDF (position X=50, Y=200)
                canvas.drawBitmap(scaledBitmap, 50, 200, paint);
                canvas.drawText("Photo du terrain :", 50, 190, paint);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        document.finishPage(page);

        // 3. Sauvegarde dans le dossier "Documents"
        // On utilise DIRECTORY_DOCUMENTS au lieu de DOWNLOADS
        File docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        // Créer le dossier s'il n'existe pas
        if (!docsDir.exists()) {
            docsDir.mkdirs();
        }

        File file = new File(docsDir, "Rapport_Technique_" + System.currentTimeMillis() + ".pdf");

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF enregistré dans Documents !", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de sauvegarde : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}