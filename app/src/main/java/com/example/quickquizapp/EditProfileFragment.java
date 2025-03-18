package com.example.quickquizapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


public class EditProfileFragment extends Fragment {

    private EditText etName, etEmail;
    private Button btnSaveChanges;
    private ImageButton btnBack, btnChangeAvatar;
    private ImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_profile, container, false);

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnBack = view.findViewById(R.id.btnBack);
        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);
        profileImage = view.findViewById(R.id.profileImage);

        // Xử lý sự kiện khi bấm nút "Save changes"
        btnSaveChanges.setOnClickListener(v -> showSuccessDialog());

        // Nút quay lại: Quay về HomeFragment khi bấm
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new HomeFragment()).commit());

        return view;
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOk = dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
