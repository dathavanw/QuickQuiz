package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_quickquiz.adapter.OnboardingAdapter;
import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.repository.UserRepository;
import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;
import com.example.btl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private Button skipButton, backButton, nextButton;
    private LinearLayout indicatorLayout;
    private int currentPage = 0;
    private SharedPreferencesManager sharedPreferencesManager;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainstart);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(this);
        userRepository = new UserRepository();

        viewPager2 = findViewById(R.id.slideViewPager);
        skipButton = findViewById(R.id.skipButton);
        backButton = findViewById(R.id.btnback);
        nextButton = findViewById(R.id.btnnext);
        indicatorLayout = findViewById(R.id.indicator_layout);

        OnboardingAdapter adapter = new OnboardingAdapter();
        viewPager2.setAdapter(adapter);

        // Skip Button Logic
        skipButton.setOnClickListener(v -> {
            startActivity(new Intent(OnboardingActivity.this, WhoUsingActivity.class));
            finish();
        });
        // Back Button Logic
        backButton.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                viewPager2.setCurrentItem(currentPage);
            }
        });
        // Next Button Logic
        nextButton.setOnClickListener(v -> {
            if (currentPage < adapter.getItemCount() - 1) {
                currentPage++;
                viewPager2.setCurrentItem(currentPage);
            } else {

                startActivity(new Intent(OnboardingActivity.this, WhoUsingActivity.class));
                finish();
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updateIndicators(position, adapter.getItemCount());
            }
        });

        // Kiểm tra người dùng đã đăng nhập chưa
        if (sharedPreferencesManager.isRememberMeChecked() && mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();

            userRepository.getUserRole(uid, new Database.RoleCallback() {
                @Override
                public void onRoleReceived(String role) {
                    if (role != null) {
                        if (role.equals("teacher")) {
                            startActivity(new Intent(OnboardingActivity.this, StudentActivity.class)); // thay băng teacher nhé
                        } else if (role.equals("student")) {
                            startActivity(new Intent(OnboardingActivity.this, StudentActivity.class));
                        } else {
                            Toast.makeText(OnboardingActivity.this, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(OnboardingActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }



    }

    private void updateIndicators(int position, int totalPages) {
        indicatorLayout.removeAllViews();

        for (int i = 0; i < totalPages; i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(i == position ? R.drawable.active_dot : R.drawable.inactive_dot);
            indicatorLayout.addView(indicator);
        }
    }




}
