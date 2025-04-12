package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.app_quickquiz.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private TextView tvTitle;
    private ImageButton btnBack, btnTogglePassword, btnToggleConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Khởi tạo các thành phần giao diện
        tvTitle = findViewById(R.id.tvTitle);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);

        // Xử lý sự kiện nút Back
        btnBack.setOnClickListener(view -> finish());

        // Toggle ẩn/hiện mật khẩu (không áp dụng transformation)
        btnTogglePassword.setOnClickListener(view -> togglePasswordVisibility(etNewPassword));
        btnToggleConfirmPassword.setOnClickListener(view -> togglePasswordVisibility(etConfirmPassword));

        // Xử lý thay đổi mật khẩu
        findViewById(R.id.btnChangePassword).setOnClickListener(view -> changePassword());
    }

    // Toggle ẩn/hiện mật khẩu (không áp dụng transformation)
    private void togglePasswordVisibility(EditText editText) {
        if (editText.getTransformationMethod() != null) {
            editText.setTransformationMethod(null);  // Hiển thị mật khẩu dưới dạng văn bản
        } else {
            editText.setTransformationMethod(new android.text.method.PasswordTransformationMethod());  // Ẩn mật khẩu
        }
    }

    // Logic thay đổi mật khẩu
    private void changePassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra nếu có trường nào trống
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu xác nhận và mật khẩu mới có khớp nhau không
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật mật khẩu trong Firebase Authentication
        if (currentUser != null) {
            currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSuccessDialog();  // Hiển thị dialog thành công khi thay đổi mật khẩu thành công
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Password change failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Hiển thị dialog khi mật khẩu thay đổi thành công
    private void showSuccessDialog() {
        // Tạo đối tượng AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout tùy chỉnh cho dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

        // Lấy nút OK trong layout
        Button btnOk = view.findViewById(R.id.btnOk);

        // Thiết lập dialog
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Xử lý sự kiện khi nhấn nút OK
        btnOk.setOnClickListener(v -> dialog.dismiss());  // Đóng dialog khi nhấn nút OK

        // Hiển thị dialog
        dialog.show();
    }
}
