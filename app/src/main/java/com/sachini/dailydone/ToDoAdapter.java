package com.sachini.dailydone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private MainActivity activity; // Store the activity reference

    // Updated Constructor to take the Activity
    public ToDoAdapter(List<ToDoModel> todoList, MainActivity activity) {
        this.todoList = todoList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());

        // Handle Checkbox state
        holder.checkbox.setOnCheckedChangeListener(null); // Clear listener before setting checked
        holder.checkbox.setChecked(item.getStatus() != 0);

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setStatus(isChecked ? 1 : 0);
            activity.saveTasks(); // Save change to SharedPreferences
        });

        // --- DELETE LOGIC ---
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            todoList.remove(currentPos);
            notifyItemRemoved(currentPos);
            notifyItemRangeChanged(currentPos, todoList.size());
            activity.saveTasks(); // Save deletion to SharedPreferences
        });

        // --- EDIT LOGIC ---
        holder.btnEdit.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            // Pass the POSITION and the TEXT to the newInstance method
            EditTask editTask = EditTask.newInstance(currentPos, item.getTask());
            editTask.show(activity.getSupportFragmentManager(), EditTask.TAG);
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public TextView task;
        public ImageView btnDelete;
        public ImageView btnEdit;

        ViewHolder(View view) {
            super(view);
            checkbox = view.findViewById(R.id.todoCheckBox);
            task = view.findViewById(R.id.todoText);
            btnDelete = view.findViewById(R.id.btnDelete);
            btnEdit = view.findViewById(R.id.btnEdit);
        }
    }
}