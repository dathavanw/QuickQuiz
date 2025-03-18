package com.example.quickquizapp;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private ImageButton btnTogglePassword, btnToggleConfirmPassword;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);

        // Xử lý sự kiện khi bấm vào icon mắt để hiển thị/ẩn mật khẩu
        btnTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            togglePasswordVisibility(etNewPassword, btnTogglePassword, isPasswordVisible);
        });

        btnToggleConfirmPassword.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            togglePasswordVisibility(etConfirmPassword, btnToggleConfirmPassword, isConfirmPasswordVisible);
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageButton button, boolean isVisible) {
        if (isVisible) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            button.setImageResource(R.drawable.ic_visibility); // Icon mắt mở 👁
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            button.setImageResource(R.drawable.ic_visibility_off); // Icon mắt đóng
        }
        editText.setSelection(editText.getText().length()); // Giữ con trỏ ở cuối văn bản
    }
}
