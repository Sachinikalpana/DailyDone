package com.sachini.dailydone;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Your design document requires an 8000ms (8 second) delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Move to the Sign-In screen after the delay
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(intent);
                finish(); // Destroy the splash screen so the user can't go back to it
            }
        }, 3000);
    }
}
