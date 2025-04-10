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
            emailGetPassword.setError("Please enter email !");
        } else if (!email.isEmpty()) {
            userRepository.sendPasswordReset(email);
            Toast.makeText(this,"Verify your email address ! ",Toast.LENGTH_SHORT).show();
            Intent intent  = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(this,"Error ! Please try again!",Toast.LENGTH_SHORT).show();
        }
    }





}
