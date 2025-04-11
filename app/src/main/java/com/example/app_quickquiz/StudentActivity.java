package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;

public class StudentActivity extends AppCompatActivity {
    private TextView btnSignOut;
    private SharedPreferencesManager sharedPreferencesManager;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnSignOut = findViewById(R.id.tvSignOut);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(this);
        btnSignOut.setOnClickListener(v -> signOut());
    }

    public void signOut(){
        mAuth.signOut();
        sharedPreferencesManager.clearLoginCredentials();
        Intent intent = new Intent(this,OnboardingActivity.class);
        startActivity(intent);
    }
}
