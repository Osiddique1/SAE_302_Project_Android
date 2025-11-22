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
    private Button loginButton;

    // Keys for passing data to the next activity
    public static final String EXTRA_FIRST_NAME = "com.example.applicationtechnicien.FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "com.example.applicationtechnicien.LAST_NAME";

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
        loginButton = findViewById(R.id.button_login);

        // 2. Set up the login button click listener
        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();

        // Basic validation: ensure both fields are not empty
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please enter both your first and last name.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Create an Intent to navigate to the HomeActivity
        /*Intent intent = new Intent(MainActivity.this, HomeActivity.class);

        // 4. Pass the entered names to the next Activity
        intent.putExtra(EXTRA_FIRST_NAME, firstName);
        intent.putExtra(EXTRA_LAST_NAME, lastName);

        // 5. Start the new Activity
        startActivity(intent);*/
    }
}
