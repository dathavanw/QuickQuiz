package com.example.quickquizapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quickquizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etName, etEmail;
    private ImageView profileImage;
    private ImageButton btnChangeAvatar, btnBack;
    private Button btnSaveChanges;

    private Uri imageUri;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Ánh xạ view
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        profileImage = findViewById(R.id.profileImage);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBack = findViewById(R.id.btnBack);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("avatars");

        // Hiển thị thông tin ban đầu
        if (currentUser != null) {
            etName.setText(currentUser.getDisplayName());
            etEmail.setText(currentUser.getEmail());

            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this).load(currentUser.getPhotoUrl()).into(profileImage);
            }
        }

        btnChangeAvatar.setOnClickListener(v -> openImagePicker());
        btnSaveChanges.setOnClickListener(v -> updateProfile());
        btnBack.setOnClickListener(v -> finish());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            StorageReference avatarRef = storageReference.child(currentUser.getUid() + ".jpg");
            avatarRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                updateFirebaseProfile(name, email, uri.toString());
                            })
                    ).addOnFailureListener(e ->
                            Toast.makeText(EditProfileActivity.this, "Lỗi tải ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            String existingPhoto = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;
            updateFirebaseProfile(name, email, existingPhoto);
        }
    }

    private void updateFirebaseProfile(String name, String email, String photoUrl) {
        UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name);
        if (photoUrl != null) {
            profileUpdates.setPhotoUri(Uri.parse(photoUrl));
        }

        currentUser.updateProfile(profileUpdates.build())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.updateEmail(email)
                                .addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful()) {
                                        // ✅ Ghi vào Realtime Database
                                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                        String uid = currentUser.getUid();

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("name", name);
                                        userMap.put("email", email);
                                        // Không ghi đè role nếu không thay đổi

                                        usersRef.child(uid).updateChildren(userMap)
                                                .addOnSuccessListener(unused -> showSuccessDialog())
                                                .addOnFailureListener(e ->
                                                        Toast.makeText(this, "Lỗi lưu Database: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                                );

                                    } else {
                                        Toast.makeText(this, "Lỗi cập nhật email: " + emailTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Lỗi cập nhật hồ sơ: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
