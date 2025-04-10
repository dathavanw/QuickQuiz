package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.repository.UserRepository;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button btnSendCodeToEmail;
    private EditText emailGetPassword;
    private UserRepository userRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailGetPassword = findViewById(R.id.txtEmailGetPassword);
        btnSendCodeToEmail = findViewById(R.id.btnSendCodeToEmail);
        userRepository = new UserRepository();

        btnSendCodeToEmail.setOnClickListener(v -> resetPassword());

    }

    public void resetPassword(){
        String email = emailGetPassword.getText().toString().trim();
        if(email.isEmpty()){
            emailGetPassword.setError("Vui lòng nhập email !");
        } else if (!email.isEmpty()) {
            userRepository.sendPasswordReset(email);
            Toast.makeText(this,"Email khôi phục đã được gửi ",Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this,"Xảy ra lỗi xác nhận ! Vui lòng thử lại ! ",Toast.LENGTH_SHORT).show();
        }
    }





}
