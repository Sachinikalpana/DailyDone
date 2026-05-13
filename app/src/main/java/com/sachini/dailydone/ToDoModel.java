package com.sachini.dailydone;

public class ToDoModel {
    private String task;
    private int status; // 0 for unchecked, 1 for checked

    public ToDoModel(String task, int status) {
        this.task = task;
        this.status = status;
    }

    // Getters
    public String getTask() { return task; }
    public int getStatus() { return status; }

    // --- SETTERS (Required for Editing) ---
    public void setTask(String task) {
        this.task = task;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}