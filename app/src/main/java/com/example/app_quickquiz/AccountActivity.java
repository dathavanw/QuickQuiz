package com.example.app_quickquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.activity.ChangePasswordActivity;
import com.example.app_quickquiz.activity.DialogRateActivity;
import com.example.app_quickquiz.activity.DialogShareActivity;
import com.example.app_quickquiz.activity.EditProfileActivity;
import com.example.app_quickquiz.activity.HistoryActivity;
import com.example.app_quickquiz.activity.NotificationActivity;
import com.example.app_quickquiz.activity.SearchQuizActivity;
import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.User;
import com.example.app_quickquiz.repository.UserRepository;
import com.example.app_quickquiz.R;
import com.example.btl.ActivityHoatDong;
import com.example.btl.GiaoVienMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private LinearLayout btnSupport;
    private Button btnEdit;
    private LinearLayout layoutNotification;
    private LinearLayout layoutChangePassword;
    private LinearLayout layoutShare;
    private LinearLayout layoutSupport;
    private LinearLayout layoutRateUs;
    private UserRepository userRepository;
    private TextView txtUsername,txtEmailUser;
    private FirebaseAuth mAuth;
    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btnEdit = findViewById(R.id.btnEdit);
        layoutNotification = findViewById(R.id.layoutNotification);
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        layoutShare = findViewById(R.id.layoutShare);
        layoutRateUs = findViewById(R.id.layoutRateUs);

        txtUsername = findViewById(R.id.textUser);
        txtEmailUser = findViewById(R.id.textEmailUser);
        userRepository = new UserRepository();
        mAuth = FirebaseAuth.getInstance();
        menuButton = findViewById(R.id.menu_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_account);

        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });

        menuButton.setOnClickListener(v -> toggleMenu());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String itemName = getResources().getResourceEntryName(item.getItemId());

            // Lấy role từ SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String role = prefs.getString("role", "student"); // mặc định là student

            switch (itemName) {
                case "nav_home":
                    if (role.equals("teacher")) {
                        startActivity(new Intent(this, GiaoVienMainActivity.class));
                    } else {
                        startActivity(new Intent(this, StudentActivity.class));
                    }
                    return true;

                case "nav_activity":
                    startActivity(new Intent(this, HistoryActivity.class));
                    return true;

                case "nav_account":
                    return true; // đang ở đây rồi

                case "nav_search":
                    startActivity(new Intent(this, SearchQuizActivity.class));
                    return true;

                case "nav_course":
                default:
                    Toast.makeText(this, "Chức năng đang được cập nhật!", Toast.LENGTH_SHORT).show();
                    return true;
            }
        });
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, DialogShareActivity.class);
                startActivity(intent);
            }
        });

        layoutRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, DialogRateActivity.class);
                startActivity(intent);
            }
        });

        btnSupport = findViewById(R.id.btnSupport);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this,  SupportActivity.class);
                startActivity(intent);
            }
        });


    }
    private void toggleMenu() {
        ObjectAnimator animatorX;
        ObjectAnimator animatorAlpha;

        if (isMenuOpen) {
            animatorX = ObjectAnimator.ofFloat(bottomNavigationView, "translationX", -bottomNavigationView.getWidth());
            animatorAlpha = ObjectAnimator.ofFloat(bottomNavigationView, "alpha", 1f, 0f);
            animatorX.setDuration(300);
            animatorAlpha.setDuration(200);
            animatorX.start();
            animatorAlpha.start();
            animatorX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bottomNavigationView.setVisibility(BottomNavigationView.GONE);
                }
            });
        } else {
            bottomNavigationView.setVisibility(BottomNavigationView.VISIBLE);
            animatorX = ObjectAnimator.ofFloat(bottomNavigationView, "translationX", 0f);
            animatorAlpha = ObjectAnimator.ofFloat(bottomNavigationView, "alpha", 0f, 1f);
            animatorX.setDuration(300);
            animatorAlpha.setDuration(200);
            animatorX.start();
            animatorAlpha.start();
        }
        isMenuOpen = !isMenuOpen;
    }
}
