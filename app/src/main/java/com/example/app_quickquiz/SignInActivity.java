package com.example.app_quickquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.repository.UserRepository;
import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;

import com.example.btl.GiaoVienMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private Button btnSignUp, btnSignInBottom;
    private EditText txtEmail, txtPassword;
    private CheckBox chkRememberMe;
    private ImageView btnHintPassword;
    private TextView txtForgotPassword, passwordWarning;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private SharedPreferencesManager sharedPreferencesManager;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        // Ánh xạ view
        userRepository = new UserRepository();
        mAuth = FirebaseAuth.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(this);

        btnSignUp = findViewById(R.id.btnSignUp);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnHintPassword = findViewById(R.id.btnHintPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        btnSignInBottom = findViewById(R.id.btnSignInBottom);
        passwordWarning = findViewById(R.id.passwordWarning);
        chkRememberMe = findViewById(R.id.checkboxRememberPassword);

        // Nếu có lưu Remember Me
        if (sharedPreferencesManager.isRememberMeChecked()) {
            txtEmail.setText(sharedPreferencesManager.getEmail());
            txtPassword.setText(sharedPreferencesManager.getPassword());
            chkRememberMe.setChecked(true);
        }

        btnSignUp.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
        txtForgotPassword.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class)));
        btnHintPassword.setOnClickListener(v -> togglePasswordVisibility());
        btnSignInBottom.setOnClickListener(v -> signIn());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnHintPassword.setImageResource(R.drawable.ic_eye_close);
        } else {
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnHintPassword.setImageResource(R.drawable.eyepassword);
        }
        isPasswordVisible = !isPasswordVisible;
        txtPassword.setSelection(txtPassword.length());
    }

    private void signIn() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.loginWithEmailAndPassword(email, password, new Database.LoginCallback() {
        @Override
        public void onSuccess(FirebaseUser user) {
                Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                // Remember Me
                if (chkRememberMe.isChecked()) {
                    sharedPreferencesManager.saveLoginCredentials(email, password, true);
                } else {
                    sharedPreferencesManager.clearLoginCredentials();
                }

                // Truy vấn role từ UID
                userRepository.getUserRole(user.getUid(), new Database.RoleCallback() {
                    @Override
                    public void onRoleReceived(String role) {
                        // Lưu vào SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        prefs.edit().putString("role", role).apply();

                        // Điều hướng
                        handleRoleNavigation(user.getUid(), role);
                    }

                    @Override
                    public void onError(String e) {
                        Toast.makeText(SignInActivity.this, "Lỗi khi lấy vai trò người dùng: " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRoleNavigation(String userId, String role) {
        Intent intent;
        if ("teacher".equals(role)) {
            Log.d("DEBUG", "Role is teacher");
            intent = new Intent(SignInActivity.this, GiaoVienMainActivity.class);
        } else if ("student".equals(role)) {
            Log.d("DEBUG", "Role is student");
            intent = new Intent(SignInActivity.this, StudentActivity.class);
        } else {
            Log.d("DEBUG", "Role is unknown");
            Toast.makeText(SignInActivity.this, "Vai trò không xác định. Vui lòng liên hệ quản trị viên.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Truyền userId qua Intent và lưu vào SharedPreferences nếu cần
        intent.putExtra("userId", userId);
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().putString("userId", userId).apply();

        startActivity(intent);
        finish();
    }
}
