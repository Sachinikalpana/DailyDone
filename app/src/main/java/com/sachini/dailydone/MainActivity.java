package com.sachini.dailydone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private List<ToDoModel> taskList;
    private ToDoAdapter adapter;
    private SharedPreferences prefs;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Initialize SharedPreferences & Get Current User
        prefs = getSharedPreferences("AppUsers", Context.MODE_PRIVATE);
        currentUserEmail = prefs.getString("current_user_email", "guest");

        // 2. Load Saved Tasks
        loadTasks();

        // Notch handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 3. Two-Tone Title
        TextView appTitle = findViewById(R.id.txtDailyDone);
        if (appTitle != null) {
            String text = "DailyDone";
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#001A4D")), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            appTitle.setText(spannable);
        }

        // 4. Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoAdapter(taskList, this);
        recyclerView.setAdapter(adapter);

        // 5. FAB - Add Task
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.fabAdd);
        if (fab != null) {
            fab.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));
        }

        // 6. Profile Click
        CircleImageView profile = findViewById(R.id.profileImg);
        if (profile != null) {
            profile.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            });
        }

        // Initial image load
        loadProfileImage();
    }

    // --- ADDED: REFRESH IMAGE WHEN RETURNING TO THIS SCREEN ---
    @Override
    protected void onResume() {
        super.onResume();
        // This ensures the photo updates immediately after you change it in UserInfoActivity
        loadProfileImage();
    }

    private void loadProfileImage() {
        CircleImageView profile = findViewById(R.id.profileImg);
        if (profile != null) {
            // Use the same key we used in UserInfoActivity
            String imageUriString = prefs.getString(currentUserEmail + "_profile_uri", null);
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                profile.setImageURI(imageUri);
            }
        }
    }

    // --- HELPER METHODS ---

    public void addTask(String taskTitle) {
        taskList.add(new ToDoModel(taskTitle, 0));
        saveTasks();
        adapter.notifyItemInserted(taskList.size() - 1);
    }

    public void editTask(int position, String updatedTitle) {
        ToDoModel item = taskList.get(position);
        item.setTask(updatedTitle);
        saveTasks();
        adapter.notifyItemChanged(position);
    }

    public void saveTasks() {
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefs.edit().putString(currentUserEmail + "_tasks", json).apply();
    }

    private void loadTasks() {
        Gson gson = new Gson();
        String json = prefs.getString(currentUserEmail + "_tasks", null);
        Type type = new TypeToken<ArrayList<ToDoModel>>() {}.getType();
        taskList = (json == null) ? new ArrayList<>() : gson.fromJson(json, type);
    }
}