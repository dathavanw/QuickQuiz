package com.example.btl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GiaoVienMainActivity extends AppCompatActivity {

    private boolean isMenuOpen = false;
    private BottomNavigationView bottomNavigationView;
    private ImageButton menuButton;
    private TextView txtCreateQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giaovien);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuButton = findViewById(R.id.menu_button);

        if (menuButton != null) {
            menuButton.setOnClickListener(v -> toggleMenu());
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView == null) {
            Log.e("DEBUG", "BottomNavigationView không được include hoặc sai ID!");
        } else {
            Log.d("DEBUG", "BottomNavigationView đã load thành công.");
        }

        txtCreateQuiz = findViewById(R.id.txtCreateQuiz);

        // Ẩn menu ban đầu
        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        menuButton.setOnClickListener(v -> toggleMenu());

        txtCreateQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(GiaoVienMainActivity.this, AddQuizActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String itemName = getResources().getResourceEntryName(item.getItemId());
            switch (itemName) {
                case "nav_home":
                    return true;
                case "nav_account":
                    startActivity(new Intent(this, SettingActivity.class));
                    return true;
                case "nav_activity":
                    startActivity(new Intent(this, ActivityHoatDong.class));
                    return true;
                case "nav_course":
                    startActivity(new Intent(this, CourseActivity.class));
                    return true;
                case "nav_search":
                    startActivity(new Intent(this, SearchActivity.class));
                    return true;
                default:
                    return false;
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
