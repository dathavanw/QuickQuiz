package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private ImageView btnPrev, btnNext, btnBack;

    private DatabaseReference questionRef, answerRef;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private List<Answer> currentAnswers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        questionRef = FirebaseDatabase.getInstance().getReference("Questions");
        answerRef = FirebaseDatabase.getInstance().getReference("Answers");

        loadQuestions();

        btnPrev.setOnClickListener(v -> showPreviousQuestion());
        btnNext.setOnClickListener(v -> showNextQuestion());
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước
    }

    private void loadQuestions() {
        questionRef.orderByChild("quiz_id").equalTo(1)  // Giả sử quiz_id = 1
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        questionList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Question q = dataSnapshot.getValue(Question.class);
                            if (q != null) {
                                questionList.add(q);
                            }
                        }
                        if (!questionList.isEmpty()) {
                            showQuestion(currentQuestionIndex);
                        } else {
                            Toast.makeText(QuizActivity.this, "Không có câu hỏi!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(QuizActivity.this, "Lỗi khi tải câu hỏi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showQuestion(int index) {
        if (index < 0 || index >= questionList.size()) return;

        Question question = questionList.get(index);
        tvQuestion.setText(question.getContent());

        loadAnswers(question.getId());
    }

    private void loadAnswers(int questionId) {
        answerRef.orderByChild("question_id").equalTo(questionId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        currentAnswers.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Answer answer = dataSnapshot.getValue(Answer.class);
                            if (answer != null) {
                                currentAnswers.add(answer);
                            }
                        }

                        displayAnswers();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(QuizActivity.this, "Lỗi khi tải đáp án", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayAnswers() {
        Button[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.VISIBLE);
            if (i < currentAnswers.size()) {
                Answer answer = currentAnswers.get(i);
                buttons[i].setText(answer.getContent());

                if (answer.isIs_correct()) {
                    buttons[i].setTextColor(btnOption1.getCurrentTextColor());
                    buttons[i].setTypeface(null, android.graphics.Typeface.BOLD);
                } else {
                    buttons[i].setTextColor(btnOption1.getCurrentTextColor());
                    buttons[i].setTypeface(null, android.graphics.Typeface.NORMAL);
                }
            } else {
                buttons[i].setText("N/A");
                buttons[i].setTypeface(null, android.graphics.Typeface.NORMAL);
            }
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questionList.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        }
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion(currentQuestionIndex);
        }
    }
}
