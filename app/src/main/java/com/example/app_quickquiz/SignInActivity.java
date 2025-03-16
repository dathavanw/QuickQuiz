
package com.example.app_quickquiz;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.InputType;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity{

    private Button btnSignUp,btnSignInBottom;
    private EditText txtEmail,txtPassword;
    private ImageView btnHintPassword;
    private TextView txtForgotPassword;
    private boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        btnSignUp = findViewById(R.id.btnSignUp);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnHintPassword = findViewById(R.id.btnHintPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this ,SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnHintPassword.setOnClickListener(v ->{
            if (isVisible) {
                txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnHintPassword.setImageResource(R.drawable.ic_eye_close);
            } else {
                txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnHintPassword.setImageResource(R.drawable.eyepassword);
            }
            isVisible = !isVisible;
            txtPassword.setSelection(txtPassword.getText().length());
        } );

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}





