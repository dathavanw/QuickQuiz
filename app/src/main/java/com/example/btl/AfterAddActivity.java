package com.example.btl;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AfterAddActivity extends AppCompatActivity {

    private LinearLayout layoutQuestionList;
    private TextView txtQuizTitle;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_add);

        layoutQuestionList = findViewById(R.id.layoutQuestionList);
        txtQuizTitle = findViewById(R.id.txtDetailTitle);
        quizId = getIntent().getStringExtra("quizId");

        loadQuizTitle();
        loadQuestions();

        Button btnSaveQuiz = findViewById(R.id.btnSaveQuiz);
        btnSaveQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(AfterAddActivity.this, ActivityHoatDong.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        Button btnCreateQuestion = findViewById(R.id.btnCreateQuestion);
        btnCreateQuestion.setOnClickListener(v -> {
            String[] types = {"Câu hỏi trắc nghiệm", "Câu hỏi điền vào chỗ trống"};
            new AlertDialog.Builder(AfterAddActivity.this)
                    .setTitle("Chọn loại câu hỏi")
                    .setItems(types, (dialog, which) -> {
                        Intent intent;
                        if (which == 0) {
                            intent = new Intent(AfterAddActivity.this, AddMultipleChoiceActivity.class);
                        } else {
                            intent = new Intent(AfterAddActivity.this, FillBlankActivity.class);
                        }
                        intent.putExtra("quizId", quizId);
                        startActivity(intent);
                    })
                    .show();
        });
    }

    private void loadQuizTitle() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        quizRef.child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String quizTitle = dataSnapshot.child("title").getValue(String.class);
                    txtQuizTitle.setText("Tên chu de: " + quizTitle);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadQuestions() {
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions");
        DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("Answers");

        questionRef.orderByChild("quiz_id").equalTo(quizId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        layoutQuestionList.removeAllViews();

                        for (DataSnapshot questionSnap : dataSnapshot.getChildren()) {
                            String questionId = questionSnap.child("id").getValue(String.class);
                            String content = questionSnap.child("content").getValue(String.class);
                            String type = questionSnap.child("question_type").getValue(String.class);

                            View cardView = LayoutInflater.from(AfterAddActivity.this)
                                    .inflate(R.layout.layout_cau_hoi_card, layoutQuestionList, false);

                            TextView txtCauHoi = cardView.findViewById(R.id.txtCauHoi);
                            LinearLayout layoutDapAn = cardView.findViewById(R.id.layoutDapAn);
                            txtCauHoi.setText("• " + content + ("fill_blank".equals(type) ? " [Điền chỗ trống]" : ""));

                            // Click để chỉnh sửa
                            cardView.setOnClickListener(v -> {
                                Intent intent;
                                if ("multiple_choice".equals(type)) {
                                    intent = new Intent(AfterAddActivity.this, AddMultipleChoiceActivity.class);
                                } else {
                                    intent = new Intent(AfterAddActivity.this, FillBlankActivity.class);
                                }
                                intent.putExtra("quizId", quizId);
                                intent.putExtra("questionId", questionId);
                                startActivity(intent);
                            });

                            answerRef.orderByChild("question_id").equalTo(questionId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot answerSnapshot) {
                                            layoutDapAn.removeAllViews();
                                            for (DataSnapshot ans : answerSnapshot.getChildren()) {
                                                String answerText = ans.child("answer").getValue(String.class);
                                                Boolean isCorrect = ans.child("is_correct").getValue(Boolean.class);

                                                TextView answerView = new TextView(AfterAddActivity.this);
                                                answerView.setTextSize(14);

                                                if ("multiple_choice".equals(type)) {
                                                    answerView.setText("    - " + answerText + (Boolean.TRUE.equals(isCorrect) ? " ✅" : ""));
                                                } else {
                                                    answerView.setText("    Đáp án đúng: " + answerText);
                                                    layoutDapAn.addView(answerView);
                                                    break;
                                                }
                                                layoutDapAn.addView(answerView);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                        }
                                    });

                            layoutQuestionList.addView(cardView);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
