package com.example.applicationtechnicien;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class activity_rapport extends AppCompatActivity {

    private TextView tvDetails;
    private Button btnPdf;
    private String projectTitle, technicianName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        // Configuration Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_rapport);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvDetails = findViewById(R.id.tv_rapport_details);
        btnPdf = findViewById(R.id.btn_generate_pdf);

        // Simulation de récupération de données
        // Note : Dans un cas réel, utilisez SharedPreferences ou une Database
        // pour récupérer les infos de Checklist et Visit Terrain
        loadData();

        btnPdf.setOnClickListener(v -> createPDF());
    }

    private void loadData() {
        // Exemple de récupération (à adapter selon votre logique de stockage)
        projectTitle = getIntent().getStringExtra(MainActivity.EXTRA_PROJECT_TITLE);
        String firstName = getIntent().getStringExtra(MainActivity.EXTRA_FIRST_NAME);
        technicianName = (firstName != null) ? firstName : "Technicien";

        String recap = "Projet : " + projectTitle + "\n" +
                "Technicien : " + technicianName + "\n\n" +
                "Statut Checklist : Complété\n" +
                "Visite Terrain : OK";

        tvDetails.setText(recap);
    }

    private void createPDF() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Écriture dans le PDF
        canvas.drawText("RAPPORT TECHNIQUE", 80, 50, paint);
        canvas.drawText("Projet : " + projectTitle, 20, 100, paint);
        canvas.drawText("Technicien : " + technicianName, 20, 120, paint);
        canvas.drawText("Observations : Visite effectuée avec succès.", 20, 150, paint);

        document.finishPage(page);

        // Enregistrement du fichier
        File filePath = new File(getExternalFilesDir(null), "Rapport_" + projectTitle + ".pdf");

        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "PDF généré : " + filePath.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la génération", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}