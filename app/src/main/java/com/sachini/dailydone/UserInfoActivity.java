package com.sachini.dailydone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri; // Added for Image URI handling
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> mGetContent;
    private CircleImageView profileImage;
    private TextView displayUsername, displayEmail;

    // Declare these at the top for easy access in all methods
    private SharedPreferences prefs;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // 1. INITIALIZE VIEWS
        profileImage = findViewById(R.id.profileImage);
        displayUsername = findViewById(R.id.displayUsername);
        displayEmail = findViewById(R.id.displayEmail);

        // 2. INITIALIZE DATA & PREFS
        prefs = getSharedPreferences("AppUsers", Context.MODE_PRIVATE);
        currentUserEmail = prefs.getString("current_user_email", "");

        // 3. LOAD SAVED DATA (Text + Photo)
        loadUserData();

        // 4. PHOTO PICKER LOGIC WITH PERSISTENCE
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Show the image immediately
                        profileImage.setImageURI(uri);

                        // --- THE PERSISTENCE TRICK ---
                        // 1. Grant permanent permission to this specific file
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        // 2. Save the URI as a string in the user's specific key
                        prefs.edit().putString(currentUserEmail + "_profile_uri", uri.toString()).apply();
                    }
                });

        profileImage.setOnClickListener(v -> mGetContent.launch("image/*"));

        // 5. SETUP UI STYLING & NAVIGATION
        setupUI();
    }

    private void loadUserData() {
        // Load Text Data
        String name = prefs.getString(currentUserEmail + "_name", "No Name Set");
        String email = prefs.getString(currentUserEmail + "_email", currentUserEmail);
        displayUsername.setText(name);
        displayEmail.setText(email);

        // Load Photo Data
        String imageUriString = prefs.getString(currentUserEmail + "_profile_uri", null);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            profileImage.setImageURI(imageUri);
        }
    }

    // Refresh UI when coming back from Edit Dialog
    public void updateUI() {
        loadUserData();
    }

    private void setupUI() {
        // Developer Link Styling
        TextView txtAboutDev = findViewById(R.id.txtAboutDev);
        String devStr = "About Developer: Click here";
        SpannableString spanDev = new SpannableString(devStr);
        spanDev.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, devStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanDev.setSpan(new UnderlineSpan(), 17, devStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtAboutDev.setText(spanDev);

        // Click Listeners
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        txtAboutDev.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, DevInfoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            EditProfileDialog editDialog = new EditProfileDialog();
            editDialog.show(getSupportFragmentManager(), "EditProfile");
        });

        findViewById(R.id.btnSignOut).setOnClickListener(v -> {
            SignOutDialog signOutDialog = new SignOutDialog();
            signOutDialog.show(getSupportFragmentManager(), "SignOut");
        });
    }
}