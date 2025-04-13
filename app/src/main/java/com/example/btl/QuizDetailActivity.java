package com.example.btl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizDetailActivity extends AppCompatActivity {

    private LinearLayout layoutQuestionList;
    private TextView txtTitle;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        layoutQuestionList = findViewById(R.id.layoutQuestionList);
        txtTitle = findViewById(R.id.txtDetailTitle);
        quizId = getIntent().getStringExtra("quizId");

        loadQuizTitle();
        loadQuestions();
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
