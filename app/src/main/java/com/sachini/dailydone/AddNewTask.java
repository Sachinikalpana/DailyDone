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

public class AddNewTask extends DialogFragment {

    public static final String TAG = "ActionDialog";

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return inflater.inflate(R.layout.new_task, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int) (280 * getResources().getDisplayMetrics().density);
            int height = (int) (240 * getResources().getDisplayMetrics().density);
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText newTaskText = view.findViewById(R.id.newTaskText);
        TextView btnOk = view.findViewById(R.id.btnOk);
        TextView btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> dismiss());

        btnOk.setOnClickListener(v -> {
            String text = newTaskText.getText().toString().trim();

            if (!text.isEmpty()) {
                // --- STEP 4 CONNECTION ---
                // We cast the activity to MainActivity to access the addTask method
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).addTask(text);
                }
                dismiss();
            } else {
                newTaskText.setError("Enter a task first");
            }
        });
    }
}