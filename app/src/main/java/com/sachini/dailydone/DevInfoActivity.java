package com.sachini.dailydone;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DevInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        Button btnBack = findViewById(R.id.btnDevBack);
        btnBack.setOnClickListener(v -> finish()); // Goes back to User Info
    }
}