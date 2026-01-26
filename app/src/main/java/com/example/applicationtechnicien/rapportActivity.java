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

    private Uri selectedImageUri;
    private ImageView imgPreview;
    private int currentY = 60; // Position verticale globale

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        imgPreview = findViewById(R.id.img_preview);
        findViewById(R.id.btn_select_photo).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        findViewById(R.id.btn_generate_pdf).setOnClickListener(v -> generatePDF());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgPreview.setImageURI(selectedImageUri);
        }
    }

    private void generatePDF() {
        SharedPreferences pTerrain = getSharedPreferences("TerrainPrefs", MODE_PRIVATE);
        SharedPreferences pCheck = getSharedPreferences("ChecklistPrefs", MODE_PRIVATE);

        PdfDocument document = new PdfDocument();
        PdfDocument.Page page = document.startPage(new PdfDocument.PageInfo.Builder(595, 842, 1).create());
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        currentY = 60;

        // --- TITRE ---
        paint.setTextSize(22f); paint.setFakeBoldText(true);
        canvas.drawText("RAPPORT D'INTERVENTION", 150, currentY, paint);

        // --- DONNÉES ---
        paint.setTextSize(14f); paint.setFakeBoldText(false);
        currentY += 60;
        drawTextLine(canvas, paint, "Lieu : " + pTerrain.getString("lieu", "N/A"));
        drawTextLine(canvas, paint, "Adresse : " + pTerrain.getString("adresse", "N/A"));
        drawTextLine(canvas, paint, "Note Terrain : " + pTerrain.getString("commentaire", "Aucune"));

        currentY += 30;
        String proprete = pCheck.getBoolean("clean_ok", false) ? "OK" : (pCheck.getBoolean("clean_rev", false) ? "À REVOIR" : "N/A");
        drawTextLine(canvas, paint, "Propreté : " + proprete);
        drawTextLine(canvas, paint, "Commentaire Checklist : " + pCheck.getString("comment", "Aucun"));

        // --- PHOTO ---
        if (selectedImageUri != null) {
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                canvas.drawBitmap(Bitmap.createScaledBitmap(bmp, 400, 300, false), 100, currentY + 30, paint);
            } catch (Exception e) { e.printStackTrace(); }
        }

        document.finishPage(page);

        // --- SAUVEGARDE DOCUMENTS ---
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Rapport_" + System.currentTimeMillis() + ".pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Enregistré dans Documents", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
        }
        document.close();
    }

    // Petite méthode pour gagner de la place
    private void drawTextLine(Canvas canvas, Paint paint, String text) {
        canvas.drawText(text, 70, currentY, paint);
        currentY += 25;
    }
}