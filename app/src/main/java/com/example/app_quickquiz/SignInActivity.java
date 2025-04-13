
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.repository.UserRepository;
import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;
import com.example.btl.GiaoVienMainActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity{

    private Button btnSignUp,btnSignInBottom;
    private EditText txtEmail,txtPassword;
    private CheckBox chkRememberMe;
    private ImageView btnHintPassword;
    private TextView txtForgotPassword,passwordWarning;
    private FirebaseAuth mAuth;
    UserRepository userRepository;
    private boolean isVisible = false;
    private SharedPreferencesManager sharedPreferencesManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        userRepository = new UserRepository();
        btnSignUp = findViewById(R.id.btnSignUp);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnHintPassword = findViewById(R.id.btnHintPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        btnSignInBottom = findViewById(R.id.btnSignInBottom);
        passwordWarning = findViewById(R.id.passwordWarning);
        chkRememberMe = findViewById(R.id.checkboxRememberPassword);

        // khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this ,SignUpActivity.class);
                startActivity(intent);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Kiểm tra nếu người dùng đã chọn "Remember me" và lưu thông tin đăng nhập
        if (sharedPreferencesManager.isRememberMeChecked()) {
            String email = sharedPreferencesManager.getEmail();
            String password = sharedPreferencesManager.getPassword();
            txtEmail.setText(email);
            txtPassword.setText(password);
            chkRememberMe.setChecked(true);
        }


        btnHintPassword.setOnClickListener(v ->eyeHintPassword());
        btnSignInBottom.setOnClickListener(v -> SignIn());
    }
    boolean isPasswordVisible = false;
    private void eyeHintPassword(){
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnHintPassword.setImageResource(R.drawable.ic_eye_close); // Mắt đóng
            isPasswordVisible = false;
        } else {
            // Hiện mật khẩu
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnHintPassword.setImageResource(R.drawable.eyepassword); // Mắt mở
            isPasswordVisible = true;
        }
        txtPassword.setSelection(txtPassword.length());
    }

    private void SignIn() {
        String   email = txtEmail.getText().toString().trim();
        String   password = txtPassword.getText().toString().trim();

        Log.d("DEBUG", "Email input: " + email);
        Log.d("DEBUG", "Password input: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        userRepository.loginWithEmailAndPassword(email, password,
                new Database.LoginCallback() {
                    @Override
                    public void onSuccess(FirebaseUser user) {
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        // Lưu thông tin đăng nhập nếu "Remember me" được chọn
                        boolean rememberMe = chkRememberMe.isChecked();
                        if (rememberMe) {
                            sharedPreferencesManager.saveLoginCredentials(email, password, true);
                        } else {
                            sharedPreferencesManager.clearLoginCredentials();
                        }
                        //truy vấn vai trò người dùng
                        userRepository.getUserRole(user.getUid(), new Database.RoleCallback() {
                            @Override
                            public void onRoleReceived(String role) {
                                handleRoleNavigation(user.getUid(), role); // điều hướng màn hình dựa trên vai trò
                            }

                            @Override
                            public void onError(String e) {
                                Toast.makeText(SignInActivity.this, "Lỗi khi lấy vai trò người dùng " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                },
                new Database.RoleCallback() {
                    @Override
                    public void onRoleReceived(String role) {
                        if (role.equals("teacher")) {
                            Log.d("DEBUG", "Role is teacher");
                            startActivity(new Intent(SignInActivity.this, StudentActivity.class));
                        } else {
                            Log.d("DEBUG", "Role is student");
                            startActivity(new Intent(SignInActivity.this, StudentActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onError(String e) {
                        Toast.makeText(SignInActivity.this, e, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void handleRoleNavigation(String userId, String role) {
        Intent intent;
        if ("teacher".equals(role)) {
            Log.d("DEBUG", "Role is teacher");
            intent = new Intent(SignInActivity.this, GiaoVienMainActivity.class); // Màn hình cho giáo viên
        } else if ("student".equals(role)) {
            Log.d("DEBUG", "Role is student");
            intent = new Intent(SignInActivity.this, StudentActivity.class); // Màn hình cho học sinh
        } else {
            Log.d("DEBUG", "Role is unknown");
            Toast.makeText(SignInActivity.this, "Vai trò không xác định. Vui lòng liên hệ quản trị viên.", Toast.LENGTH_SHORT).show();
            return; // Dừng tại đây nếu không xác định được role
        }

        // Truyền userId sang màn hình đích
        intent.putExtra("userId", userId);
        Log.d("USER_ID TỪ MÀN HÌNH ĐĂNG NHẬP ", "Giá Trị: " + userId);
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        preferences.edit().putString("userId", userId).apply();
        startActivity(intent);
        finish();
    }
}