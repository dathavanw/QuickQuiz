package com.example.app_quickquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.model.User;
import com.example.app_quickquiz.repository.UserRepository;

public class SignUpActivity extends AppCompatActivity {

    EditText name ,password, cfpassword ,email;
    Button btnSignUp;
    UserRepository userRepository;
    TextView passwordWarning;
    ImageButton btnHintPassword,btnHintCPassword;
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

        name = findViewById(R.id.txtName);
        password = findViewById(R.id.txtPassword);
        cfpassword = findViewById(R.id.txtCPassword);
        email = findViewById(R.id.txtEmailSignup);
        passwordWarning = findViewById(R.id.passwordWarning);
        btnHintPassword = findViewById(R.id.btnHintPassword);
        btnHintCPassword = findViewById(R.id.btnHintCPassword);

        btnHintPassword.setOnClickListener(v->hintPass());
        btnHintCPassword.setOnClickListener(v->hintCPass());

        btnSignUp = findViewById(R.id.btnSignUpBottom);
        btnSignUp.setOnClickListener(v -> signUp());

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
        String selectedRole = getIntent().getStringExtra("role");

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
            userRepository.insertUser(Name,Email,Password,selectedRole);
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    }
}
