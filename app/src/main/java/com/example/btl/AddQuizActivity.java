package com.example.btl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.animation.ObjectAnimator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddQuizActivity extends AppCompatActivity {

    private EditText etQuizTitle;
    private Spinner spinnerTimeLimit;
    private MaterialButton btnMultipleChoice, btnFillBlank;
    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        menuButton = findViewById(R.id.menu_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });

        menuButton.setOnClickListener(v -> toggleMenu());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (getResources().getResourceEntryName(item.getItemId())) {
                case "nav_home":
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                case "nav_activity":
                    startActivity(new Intent(this, ActivityHoatDong.class));
                    return true;
                case "nav_account":
                    startActivity(new Intent(this, SettingActivity.class));
                    return true;
                default:
                    return false;
            }
        });

        etQuizTitle = findViewById(R.id.etQuizTitle);
        spinnerTimeLimit = findViewById(R.id.spinnerTimeLimit);
        btnMultipleChoice = findViewById(R.id.btnMultipleChoice);
        btnFillBlank = findViewById(R.id.btnFillBlank);

        List<Integer> timeOptions = new ArrayList<>();
        timeOptions.add(30);
        timeOptions.add(60);
        timeOptions.add(120);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeLimit.setAdapter(adapter);

        btnMultipleChoice.setOnClickListener(v -> createQuizIfNeededAndOpen("multiple"));
        btnFillBlank.setOnClickListener(v -> createQuizIfNeededAndOpen("fill"));
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

    private void createQuizIfNeededAndOpen(String type) {
        String quizTitle = etQuizTitle.getText().toString().trim();
        int timeLimit = (Integer) spinnerTimeLimit.getSelectedItem();

        if (quizTitle.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên quiz và chọn thời gian", Toast.LENGTH_SHORT).show();
            return;
        }

        quizId = String.valueOf(System.currentTimeMillis());

        Map<String, Object> quizMap = new HashMap<>();
        quizMap.put("id", quizId);
        quizMap.put("title", quizTitle);
        quizMap.put("time_limit", timeLimit);
        quizMap.put("category_id", 1);
        quizMap.put("created_by", 2);
        quizMap.put("description", "A basic knowledge quiz.");

        String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());
        quizMap.put("created_at", currentTime);

        FirebaseDatabase.getInstance()
                .getReference("Quizzes")
                .child(quizId)
                .setValue(quizMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Quiz đã được lưu", Toast.LENGTH_SHORT).show();
                    openQuestionActivity(type);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi lưu quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void openQuestionActivity(String type) {
        Intent intent;
        if (type.equals("multiple")) {
            intent = new Intent(this, AddMultipleChoiceActivity.class);
        } else {
            intent = new Intent(this, FillBlankActivity.class);
        }
        intent.putExtra("quizId", quizId);
        intent.putExtra("quizTitle", etQuizTitle.getText().toString().trim());
        startActivity(intent);
    }
}
