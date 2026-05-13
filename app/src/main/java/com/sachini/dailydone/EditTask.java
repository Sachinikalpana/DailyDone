package com.sachini.dailydone;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditTask extends DialogFragment {

    public static final String TAG = "EditDialog";
    private int position;
    private String currentText;

    // Modified newInstance to receive the task data
    public static EditTask newInstance(int position, String text) {
        EditTask fragment = new EditTask();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the data passed from the Adapter/Activity
        if (getArguments() != null) {
            position = getArguments().getInt("position");
            currentText = getArguments().getString("text");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return inflater.inflate(R.layout.edit_task, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width = (int) (280 * getResources().getDisplayMetrics().density);
            int height = (int) (240 * getResources().getDisplayMetrics().density);
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTaskText = view.findViewById(R.id.editTaskText);
        TextView btnUpdate = view.findViewById(R.id.btnUpdate);
        TextView btnCancel = view.findViewById(R.id.btnCancelEdit);

        // Pre-fill with the existing task text
        editTaskText.setText(currentText);

        btnCancel.setOnClickListener(v -> dismiss());

        btnUpdate.setOnClickListener(v -> {
            String updatedText = editTaskText.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                // --- STEP 4 CONNECTION ---
                // Tell MainActivity to update the specific task at 'position'
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).editTask(position, updatedText);
                }
                dismiss();
            } else {
                editTaskText.setError("Task cannot be empty");
            }
        });
    }
}