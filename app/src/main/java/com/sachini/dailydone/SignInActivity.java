package com.sachini.dailydone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // 1. Find the Inputs, Buttons, and Text
        EditText usernameInput = findViewById(R.id.input_username);
        EditText passwordInput = findViewById(R.id.input_password);
        Button signInButton = findViewById(R.id.button_sign_in);
        TextView signUpText = findViewById(R.id.text_sign_up);

        // --- STYLING CODE ---
        String fullText = "Don't have an account? Sign Up";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new UnderlineSpan(), 0, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 23, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.setText(spannableString);

        // 2. Login Logic
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = usernameInput.getText().toString().trim();
                String typedPassword = passwordInput.getText().toString().trim();

                if (input.isEmpty() || typedPassword.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("AppUsers", Context.MODE_PRIVATE);

                // --- SMART SEARCH ---
                // Try to find if the input is a Username that has a linked Email
                // If it's not a username, it assumes it's an Email
                String emailToUse = prefs.getString(input + "_linkedEmail", input);

                // Now look for the password using the correct Email key
                String storedPassword = prefs.getString(emailToUse + "_password", null);

                if (storedPassword != null && storedPassword.equals(typedPassword)) {
                    // SUCCESS!
                    // Save the email as the current session
                    prefs.edit().putString("current_user_email", emailToUse).apply();

                    Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // FAIL
                    Toast.makeText(SignInActivity.this, "Invalid Username/Email or Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3. Go to SignUp Screen
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}