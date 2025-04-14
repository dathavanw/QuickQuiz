package com.example.app_quickquiz.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser currentUser;
    private Uri avatarUri;

    private static final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Firebase Auth
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        profileImage = findViewById(R.id.profileImage);


        // Kiểm tra và in UID hiện tại của người dùng
        Log.d(TAG, "Current UID: " + userId);

        // Load dữ liệu người dùng
        loadUserProfile(userId);

        // Quay lại
        btnBack.setOnClickListener(v -> finish());

        // Chọn ảnh mới
        btnChangeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        // Lưu thay đổi
        btnSaveChanges.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                updateUserProfile(userId, name, email);
            }
        });
    }

    // Load profile người dùng
    private void loadUserProfile(String userId) {
        Log.d(TAG, "Loading user profile for UID: " + userId);

        // Truy vấn Firebase theo UID
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        etName.setText(user.getName());
                        etEmail.setText(user.getEmail());
                    }

                    // Kiểm tra avatarUrl trong Firebase
                    String avatarUrl = snapshot.child("avatarUrl").getValue(String.class);
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Glide.with(EditProfileActivity.this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.ic_avatar)
                                .into(profileImage);
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "No user data found", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No user data found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Failed to load profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cập nhật thông tin người dùng
    private void updateUserProfile(String userId, String name, String email) {
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("name", name);
        updateData.put("email", email);

        if (avatarUri != null) {
            uploadAvatarAndUpdateProfile(userId, updateData);
        } else {
            databaseReference.child(userId).updateChildren(updateData)
                    .addOnSuccessListener(unused -> showSuccessDialog())
                    .addOnFailureListener(e -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
        }
    }

    // Upload avatar và cập nhật DB
    private void uploadAvatarAndUpdateProfile(String userId, HashMap<String, Object> updateData) {
        StorageReference avatarRef = storageReference.child(userId + "_avatar.jpg");
        avatarRef.putFile(avatarUri)
                .addOnSuccessListener(taskSnapshot ->
                        avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            updateData.put("avatarUrl", uri.toString());
                            databaseReference.child(userId).updateChildren(updateData)
                                    .addOnSuccessListener(unused -> showSuccessDialog())
                                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show());
                        }))
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload avatar", Toast.LENGTH_SHORT).show());
    }

    // Hiển thị dialog thành công
    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button btnOk = dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            finish(); // Quay lại màn hình trước
        });

        dialog.show();
    }

    // Nhận ảnh đại diện
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            avatarUri = data.getData();
            profileImage.setImageURI(avatarUri);
        }
    }
}
