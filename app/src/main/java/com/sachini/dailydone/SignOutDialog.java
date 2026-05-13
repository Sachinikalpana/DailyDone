package com.sachini.dailydone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SignOutDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Link the Java file to your dialog_sign_out.xml layout
        View view = inflater.inflate(R.layout.dialog_sign_out, container, false);

        // 2. Find the "Yes" button and add the logic
        TextView btnYes = view.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v -> {
            // This takes the user back to the sign-in screen
            Intent intent = new Intent(getActivity(), SignInActivity.class);

            // This line clears the "backstack" so they can't click 'back' to see their tasks again
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            dismiss(); // Closes the popup
        });

        // 3. Find the "No" button so users can stay on the profile
        TextView btnNo = view.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(v -> {
            dismiss(); // Simply closes the popup
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // 1. Make the background transparent so the rounded corners show
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // 2. Convert DP to Pixels for the exact 280x240 size
            float density = getResources().getDisplayMetrics().density;
            int widthPx = (int) (280 * density);  // 280dp
            int heightPx = (int) (240 * density); // 240dp

            // 3. Apply the exact size
            getDialog().getWindow().setLayout(widthPx, heightPx);
        }
    }
}