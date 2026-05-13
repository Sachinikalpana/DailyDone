package com.sachini.dailydone;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditProfileDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use the XML layout you provided (dialog_edit_profile.xml)
        View view = inflater.inflate(R.layout.dialog_edit_profile, container, false);

        // 1. Link the inputs
        EditText editName = view.findViewById(R.id.editUsername);
        EditText editEmail = view.findViewById(R.id.editEmail);
        TextView btnOk = view.findViewById(R.id.btnProfileOk);
        TextView btnCancel = view.findViewById(R.id.btnProfileCancel);

        // 2. Pre-fill the fields with current data so the user knows what they are editing
        SharedPreferences prefs = getActivity().getSharedPreferences("AppUsers", Context.MODE_PRIVATE);
        String currentUserEmail = prefs.getString("current_user_email", "");

        editName.setText(prefs.getString(currentUserEmail + "_name", ""));
        editEmail.setText(prefs.getString(currentUserEmail + "_email", currentUserEmail));

        // Cancel just closes the box
        btnCancel.setOnClickListener(v -> dismiss());

        // 3. Ok will save data and refresh the background screen
        btnOk.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            if (!newName.isEmpty()) {
                // Save the new data to SharedPreferences using the unique user key
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(currentUserEmail + "_name", newName);
                editor.putString(currentUserEmail + "_email", newEmail);
                editor.apply();

                // 4. Update the UserInfoActivity screen immediately
                if (getActivity() instanceof UserInfoActivity) {
                    ((UserInfoActivity) getActivity()).updateUI();
                }

                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            float density = getResources().getDisplayMetrics().density;
            int widthPx = (int) (320 * density);  // Increased slightly to fit content better
            int heightPx = ViewGroup.LayoutParams.WRAP_CONTENT; // Better to use wrap_content for dialogs

            getDialog().getWindow().setLayout(widthPx, heightPx);
        }
    }
}