package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_quickquiz.adapter.OnboardingAdapter;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private Button skipButton, backButton, nextButton;
    private LinearLayout indicatorLayout;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainstart);

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
        // Update indicator when page changes
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updateIndicators(position, adapter.getItemCount());
            }
        });
    }
    // Update indicators (dot navigation)
    private void updateIndicators(int position, int totalPages) {
        indicatorLayout.removeAllViews();

        for (int i = 0; i < totalPages; i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(i == position ? R.drawable.active_dot : R.drawable.inactive_dot);
            indicatorLayout.addView(indicator);
        }
    }
}
