package com.example.btl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.app_quickquiz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizDetailActivity extends AppCompatActivity {

    private LinearLayout layoutQuestionList;
    private TextView txtTitle;
    private String quizId;
    private GridLayout quizListLayout;
    private Button btnCreateQuiz;

    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        layoutQuestionList = findViewById(R.id.layoutQuestionList);
        txtTitle = findViewById(R.id.txtDetailTitle);
        quizId = getIntent().getStringExtra("quizId");
        menuButton = findViewById(R.id.menu_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_activity);

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
                    startActivity(new Intent(QuizDetailActivity.this, GiaoVienMainActivity.class));
                    return true;
                case "nav_account":
                    startActivity(new Intent(QuizDetailActivity.this, SettingActivity.class));
                    return true;
                case "nav_course":
                    startActivity(new Intent(QuizDetailActivity.this, CourseActivity.class));
                    return true;
                case "nav_search":
                    startActivity(new Intent(QuizDetailActivity.this, SearchActivity.class));
                    return true;
                case "nav_activity":
                    return true;
                default:
                    return false;
            }
        });
        loadQuizTitle();
        loadQuestions();
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

    private void loadQuizTitle() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        quizRef.child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                txtTitle.setText("Quiz: " + title);


            }

            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }

    private void loadQuestions() {
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions");
        DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("Answers");

        questionRef.orderByChild("quiz_id").equalTo(quizId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot questionSnapshot) {
                        layoutQuestionList.removeAllViews();

                        for (DataSnapshot questionSnap : questionSnapshot.getChildren()) {
                            String questionId = questionSnap.child("id").getValue(String.class);
                            String content = questionSnap.child("content").getValue(String.class);
                            String type = questionSnap.child("question_type").getValue(String.class);

                            View cardView = LayoutInflater.from(QuizDetailActivity.this)
                                    .inflate(R.layout.layout_cau_hoi_card, layoutQuestionList, false);

                            TextView txtCauHoi = cardView.findViewById(R.id.txtCauHoi);
                            LinearLayout layoutDapAn = cardView.findViewById(R.id.layoutDapAn);
                            txtCauHoi.setText("• " + content + ("fill_blank".equals(type) ? " [Điền chỗ trống]" : ""));

                            // Cho phép sửa câu hỏi khi click vào card
                            cardView.setOnClickListener(v -> {
                                Intent intent;
                                if ("multiple_choice".equals(type)) {
                                    intent = new Intent(QuizDetailActivity.this, AddMultipleChoiceActivity.class);
                                } else {
                                    intent = new Intent(QuizDetailActivity.this, FillBlankActivity.class);
                                }
                                intent.putExtra("quizId", quizId);
                                intent.putExtra("questionId", questionId);
                                startActivity(intent);
                            });

                            answerRef.orderByChild("question_id").equalTo(questionId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot answerSnapshot) {
                                            for (DataSnapshot ans : answerSnapshot.getChildren()) {
                                                String answerText = ans.child("answer").getValue(String.class);
                                                Boolean isCorrect = ans.child("is_correct").getValue(Boolean.class);

                                                TextView tvAnswer = new TextView(QuizDetailActivity.this);
                                                tvAnswer.setTextSize(14);

                                                if ("multiple_choice".equals(type)) {
                                                    tvAnswer.setText("    - " + answerText + (Boolean.TRUE.equals(isCorrect) ? " ✅" : ""));
                                                } else {
                                                    tvAnswer.setText("    Đáp án đúng: " + answerText);
                                                }
                                                layoutDapAn.addView(tvAnswer);

                                                if ("fill_blank".equals(type)) break;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {}
                                    });

                            layoutQuestionList.addView(cardView);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
