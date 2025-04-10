
package com.example.app_quickquiz;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity{

    private Button btnSignUp,btnSignInBottom;
    private EditText txtEmail,txtPassword;
    private ImageView btnHintPassword;
    private TextView txtForgotPassword,passwordWarning;
    private FirebaseAuth mAuth;
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
        btnSignInBottom = findViewById(R.id.btnSignInBottom);
        passwordWarning = findViewById(R.id.passwordWarning);

        // khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();

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
         String email = txtEmail.getText().toString().trim();
         String password = txtPassword.getText().toString().trim();


     }
}





