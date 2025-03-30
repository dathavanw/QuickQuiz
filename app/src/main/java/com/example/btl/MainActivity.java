package com.example.btl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private boolean isMenuOpen = false;
    private BottomNavigationView bottomNavigationView;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.giaovienmain); // Hoặc layout bạn muốn sử dụng

        // Áp dụng insets nếu có
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        menuButton = findViewById(R.id.menu_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ẩn thanh menu lúc đầu
        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });

        // Bắt sự kiện click để mở/đóng menu
        menuButton.setOnClickListener(v -> toggleMenu());
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
