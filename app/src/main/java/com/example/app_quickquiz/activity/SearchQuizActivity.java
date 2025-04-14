package com.example.app_quickquiz.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.AccountActivity;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.StudentActivity;
import com.example.app_quickquiz.adapter.QuizAdapter;
import com.example.app_quickquiz.model.Quiz;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class SearchQuizActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerViewQuiz;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList = new ArrayList<>();
    private DatabaseReference quizRef;

    private ImageView quizImage;
    private TextView tvNoResults;

    private Button btnGrades, btnSubject, btnChatGpt, mathButton, scienceButton;
    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ánh xạ view
        searchEditText = findViewById(R.id.searchEditText);
        recyclerViewQuiz = findViewById(R.id.recyclerViewQuiz);
        quizImage = findViewById(R.id.quizImage);
        tvNoResults = findViewById(R.id.tvNoResults);

        btnGrades = findViewById(R.id.btnGrades);
        btnSubject = findViewById(R.id.btnSubject);
        btnChatGpt = findViewById(R.id.btnChatGpt);
        mathButton = findViewById(R.id.mathButton);
        scienceButton = findViewById(R.id.scienceButton);

        recyclerViewQuiz.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter = new QuizAdapter(quizList);
        recyclerViewQuiz.setAdapter(quizAdapter);

        quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");

        // Load tất cả quiz khi mở app
        loadAllQuizzes();

        // Lắng nghe nhập liệu tìm kiếm
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim().toLowerCase();
                searchQuizByKeyword(keyword);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Gán sự kiện click cho các nút danh mục
        btnGrades.setOnClickListener(v -> searchQuizByCategory("Grades"));
        btnSubject.setOnClickListener(v -> searchQuizByCategory("Subject"));
        btnChatGpt.setOnClickListener(v -> searchQuizByCategory("Chat GPT"));
        mathButton.setOnClickListener(v -> searchQuizByCategory("Math"));
        scienceButton.setOnClickListener(v -> searchQuizByCategory("Language"));

        menuButton = findViewById(R.id.menu_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });

        menuButton.setOnClickListener(v -> toggleMenu());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String itemName = getResources().getResourceEntryName(item.getItemId());
            switch (itemName) {
                case "nav_home":
                    startActivity(new Intent(this, StudentActivity.class));
                    return true;

                case "nav_activity":
                    startActivity(new Intent(this, HistoryActivity.class));
                    return true;
                case "nav_account":
                    startActivity(new Intent(this, AccountActivity.class));
                    return true;
                case "nav_search":
                    return true;
                case "nav_course":

                default:
                    Toast.makeText(this, "Chức năng đang được cập nhật!", Toast.LENGTH_SHORT).show();
                    return true;
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

    private void loadAllQuizzes() {
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Quiz quiz = dataSnapshot.getValue(Quiz.class);
                    if (quiz != null) {
                        quizList.add(quiz);
                        Log.d("FIREBASE", "Quiz loaded: " + quiz.getTitle());
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Firebase error: " + error.getMessage());
            }
        });
    }


    private void searchQuizByKeyword(String keyword) {
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Quiz quiz = dataSnapshot.getValue(Quiz.class);
                    if (quiz != null) {
                        if (quiz.getTitle().toLowerCase().contains(keyword)
                                || quiz.getDescription().toLowerCase().contains(keyword)) {
                            quizList.add(quiz);
                        }
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchQuizActivity.this, "Search error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchQuizByCategory(String categoryName) {
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Quiz quiz = dataSnapshot.getValue(Quiz.class);
                    if (quiz != null && quiz.getTitle().toLowerCase().contains(categoryName.toLowerCase())) {
                        quizList.add(quiz);
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchQuizActivity.this, "Category search error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        quizAdapter.notifyDataSetChanged();

        if (quizList.isEmpty()) {
            recyclerViewQuiz.setVisibility(View.GONE);
            quizImage.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.VISIBLE);
            tvNoResults.setText("No quizzes found.");
        } else {
            recyclerViewQuiz.setVisibility(View.VISIBLE);
            quizImage.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.GONE);
        }
    }

}
