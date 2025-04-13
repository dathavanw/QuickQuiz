package com.example.app_quickquiz.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.app_quickquiz.R;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton btnBack, btnChangeAvatar;
    private EditText etName, etEmail;
    private Button btnSaveChanges;
    private ImageView profileImage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userId = "1";  // Giả sử userId là "1"
    private Uri avatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnBack = findViewById(R.id.btnBack);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        profileImage = findViewById(R.id.profileImage);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        loadUserProfile();

        btnBack.setOnClickListener(v -> onBackPressed());

        btnChangeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        btnSaveChanges.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                updateUserProfile(name, email);
            }
        });
    }

    private void loadUserProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String avatarUrl = snapshot.child("avatarUrl").getValue(String.class);

                etName.setText(name);
                etEmail.setText(email);

                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                    Glide.with(EditProfileActivity.this)
                            .load(avatarUrl)
                            .placeholder(R.drawable.ic_avatar)
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile(String name, String email) {
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("name", name);
        updateData.put("email", email);

        if (avatarUri != null) {
            uploadAvatarAndUpdateProfile(updateData);
        } else {
            databaseReference.updateChildren(updateData, (error, ref) -> {
                if (error == null) {
                    showSuccessDialog();
                } else {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void uploadAvatarAndUpdateProfile(HashMap<String, Object> updateData) {
        StorageReference avatarRef = storageReference.child(userId + "_avatar.jpg");
        avatarRef.putFile(avatarUri)
                .addOnSuccessListener(taskSnapshot -> avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    updateData.put("avatarUrl", uri.toString());

                    databaseReference.updateChildren(updateData, (error, ref) -> {
                        if (error == null) {
                            showSuccessDialog();
                        } else {
                            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload avatar", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            avatarUri = data.getData();
            profileImage.setImageURI(avatarUri);
        }
    }

    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button btnOk = dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
