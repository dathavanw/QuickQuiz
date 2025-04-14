package com.example.app_quickquiz;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;


import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.model.User;
import com.example.app_quickquiz.repository.UserRepository;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class SignUpActivity extends AppCompatActivity {

    EditText name ,password, cfpassword ,email;
    Button btnSignUp;
    UserRepository userRepository;

    TextView passwordWarning;
    ImageButton btnHintPassword,btnHintCPassword;
    ImageView btngoogle;

    private String selectedRole = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this , SignInActivity.class);
                startActivity(intent);
            }
        });

        userRepository = new UserRepository();
        selectedRole = getIntent().getStringExtra("role");

        name = findViewById(R.id.txtName);
        password = findViewById(R.id.txtPassword);
        cfpassword = findViewById(R.id.txtCPassword);
        email = findViewById(R.id.txtEmailSignup);
        passwordWarning = findViewById(R.id.passwordWarning);
        btnHintPassword = findViewById(R.id.btnHintPassword);
        btnHintCPassword = findViewById(R.id.btnHintCPassword);
        btngoogle = findViewById(R.id.btnGoogle);
        btnHintPassword.setOnClickListener(v->hintPass());
        btnHintCPassword.setOnClickListener(v->hintCPass());

        btnSignUp = findViewById(R.id.btnSignUpBottom);
        btnSignUp.setOnClickListener(v -> signUp());
        btngoogle.setOnClickListener(v -> startGoogleSignIn());

    }


    boolean isPasswordVisible = false;
    private  void hintPass(){
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnHintPassword.setImageResource(R.drawable.ic_eye_close); // Mắt đóng
            isPasswordVisible = false;
        } else {
            // Hiện mật khẩu
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnHintPassword.setImageResource(R.drawable.eyepassword); // Mắt mở
            isPasswordVisible = true;
        }

        password.setSelection(password.length());
    }

    private  void hintCPass(){
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            cfpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnHintCPassword.setImageResource(R.drawable.ic_eye_close); // Mắt đóng
            isPasswordVisible = false;
        } else {
            // Hiện mật khẩu
            cfpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnHintCPassword.setImageResource(R.drawable.eyepassword); // Mắt mở
            isPasswordVisible = true;
        }
        cfpassword.setSelection(cfpassword.length());
    }


    private void signUp() {
        String Name = name.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Cfpassword = cfpassword.getText().toString().trim();

        if (Name.isEmpty() || Password.isEmpty() || Email.isEmpty() || Cfpassword.isEmpty()) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
        } else if (!Password.equals(Cfpassword)) {
            passwordWarning.setVisibility(View.VISIBLE);
            Animation shake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.shake);
            cfpassword.startAnimation(shake);
            passwordWarning.startAnimation(shake);
            cfpassword.requestFocus();
            Toast.makeText(this, "Please re-enter password", Toast.LENGTH_SHORT).show();
        } else {

            userRepository.isEmailExists(Email, exists -> {
                if (exists) {
                    Toast.makeText(SignUpActivity.this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                } else {
                    userRepository.insertUser(Name, Email, Password, selectedRole);
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                }
            });

        }
    }

    private void startGoogleSignIn() {


    }
}