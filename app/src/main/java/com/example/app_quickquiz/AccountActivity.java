package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.User;
import com.example.app_quickquiz.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private LinearLayout btnSupport;
    private UserRepository userRepository;
    private TextView txtUsername,txtEmailUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        txtUsername = findViewById(R.id.textUser);
        txtEmailUser = findViewById(R.id.textEmailUser);
        userRepository = new UserRepository();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser() ;

        if(currentUser != null){
            String uid = currentUser.getUid();
            userRepository.getUserInfor(uid, new Database.DatabaseCallback() {
                @Override
                public void onSuccess(User user) {
                    txtUsername.setText(user.getName());
                    txtEmailUser.setText(user.getEmail());
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(AccountActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnSupport = findViewById(R.id.btnSupport);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this,  SupportActivity.class);
                startActivity(intent);
            }
        });

    }
}
