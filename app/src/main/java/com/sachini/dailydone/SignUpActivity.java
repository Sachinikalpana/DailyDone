package com.sachini.dailydone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 1. Link the variables to your XML IDs
        EditText usernameInput = findViewById(R.id.reg_username);
        EditText emailInput = findViewById(R.id.reg_email);
        EditText passwordInput = findViewById(R.id.reg_password);
        EditText confirmPasswordInput = findViewById(R.id.reg_confirm_password);
        Button signupBtn = findViewById(R.id.button_signup_submit);

        signupBtn.setOnClickListener(v -> {
            // 2. Get the values from the fields
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            // 3. Validation Logic
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Save to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("AppUsers", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Master Key: Always use Email to save the profile
            editor.putString(email + "_password", password);
            editor.putString(email + "_name", username);
            editor.putString(email + "_email", email);

            // Bridge: Link the Username to the Email for Login
            editor.putString(username + "_linkedEmail", email);

            editor.apply();

            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();

            // 5. Navigate to SignIn screen
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }
}