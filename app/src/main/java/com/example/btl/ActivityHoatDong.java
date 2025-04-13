package com.example.btl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityHoatDong extends AppCompatActivity {

    private GridLayout quizListLayout;
    private Button btnCreateQuiz;
    private TextView txtTitle;
    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoat_dong);

        quizListLayout = findViewById(R.id.quizListContainer);
        btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        txtTitle = findViewById(R.id.txtActivityTitle);
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
                    startActivity(new Intent(ActivityHoatDong.this, MainActivity.class));
                    return true;
                case "nav_account":
                    startActivity(new Intent(ActivityHoatDong.this, SettingActivity.class));
                    return true;
                case "nav_course":
                    startActivity(new Intent(ActivityHoatDong.this, CourseActivity.class));
                    return true;
                case "nav_search":
                    startActivity(new Intent(ActivityHoatDong.this, SearchActivity.class));
                    return true;
                case "nav_activity":
                    return true;
                default:
                    return false;
            }
        });

        btnCreateQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityHoatDong.this, AddQuizActivity.class);
            startActivity(intent);
        });

        loadQuizList();
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

    private void loadQuizList() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        quizListLayout.removeAllViews();

        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot quizSnap : dataSnapshot.getChildren()) {
                    String quizTitle = quizSnap.child("title").getValue(String.class);
                    String quizId = quizSnap.child("id").getValue(String.class);
                    if (quizTitle == null || quizId == null) continue;

                    DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions");
                    questionRef.orderByChild("quiz_id").equalTo(quizId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    int count = (int) snapshot.getChildrenCount();

                                    CardView cardView = new CardView(ActivityHoatDong.this);
                                    cardView.setRadius(24f);
                                    cardView.setCardElevation(10f);
                                    cardView.setUseCompatPadding(true);

                                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                    params.width = GridLayout.LayoutParams.MATCH_PARENT;
                                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                    params.setMargins(24, 24, 24, 24);
                                    cardView.setLayoutParams(params);

                                    LinearLayout innerLayout = new LinearLayout(ActivityHoatDong.this);
                                    innerLayout.setOrientation(LinearLayout.VERTICAL);
                                    innerLayout.setPadding(36, 36, 36, 36);
                                    innerLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView titleText = new TextView(ActivityHoatDong.this);
                                    titleText.setText(quizTitle);
                                    titleText.setTextSize(18);
                                    titleText.setTextColor(ContextCompat.getColor(ActivityHoatDong.this, android.R.color.black));
                                    titleText.setGravity(Gravity.CENTER);
                                    innerLayout.addView(titleText);

                                    TextView questionCount = new TextView(ActivityHoatDong.this);
                                    questionCount.setText(count + " Qs");
                                    questionCount.setTextSize(14);
                                    questionCount.setGravity(Gravity.CENTER);
                                    innerLayout.addView(questionCount);

                                    cardView.setOnClickListener(v -> {
                                        new AlertDialog.Builder(ActivityHoatDong.this)
                                                .setTitle("Chọn hành động")
                                                .setMessage("Bạn muốn làm gì với quiz này?")
                                                .setPositiveButton("Xem chi tiết", (dialog, which) -> {
                                                    Intent intent = new Intent(ActivityHoatDong.this, QuizDetailActivity.class);
                                                    intent.putExtra("quizId", quizId);
                                                    startActivity(intent);
                                                })
                                                .setNegativeButton("Xoá", (dialog, which) -> {
                                                    // Xoá quiz
                                                    FirebaseDatabase.getInstance().getReference("Quizzes").child(quizId).removeValue();

                                                    // Xoá tất cả câu hỏi liên quan đến quiz
                                                    FirebaseDatabase.getInstance().getReference("Questions")
                                                            .orderByChild("quiz_id").equalTo(quizId)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {
                                                                    for (DataSnapshot q : snapshot.getChildren()) {
                                                                        String questionId = q.child("id").getValue(String.class);
                                                                        q.getRef().removeValue();

                                                                        // Xoá đáp án của câu hỏi
                                                                        if (questionId != null) {
                                                                            FirebaseDatabase.getInstance().getReference("Answers")
                                                                                    .orderByChild("question_id").equalTo(questionId)
                                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot ansSnapshot) {
                                                                                            for (DataSnapshot ans : ansSnapshot.getChildren()) {
                                                                                                ans.getRef().removeValue();
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError error) {}
                                                                                    });
                                                                        }
                                                                    }

                                                                    // Sau khi đã xóa tất cả, mới gọi load lại danh sách
                                                                    loadQuizList();
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError error) {}
                                                            });
                                                })
                                                .setNeutralButton("Huỷ", null)
                                                .show();
                                    });

                                    cardView.addView(innerLayout);
                                    quizListLayout.addView(cardView);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {}
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadQuizList();
    }
}
