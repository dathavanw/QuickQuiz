package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;
import com.example.app_quickquiz.model.Quiz;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvQuizCode;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private ImageView btnPrev, btnNext, btnBack;

    private DatabaseReference quizRef, questionRef, answerRef;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private List<Answer> currentAnswers = new ArrayList<>();
    private int quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuizCode = findViewById(R.id.tvQuizCode);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        questionRef = FirebaseDatabase.getInstance().getReference("Questions");
        answerRef = FirebaseDatabase.getInstance().getReference("Answers");

        // Giả sử quizId được truyền vào từ Intent
        quizId = getIntent().getIntExtra("QUIZ_ID", 1);  // lấy quiz_id từ Intent hoặc set mặc định là 1

        loadQuizInfo();
        loadQuestions();

        btnPrev.setOnClickListener(v -> showPreviousQuestion());
        btnNext.setOnClickListener(v -> showNextQuestion());
        btnBack.setOnClickListener(v -> onBackPressed());  // Quay lại trang trước khi nhấn nút Back
    }

    private void loadQuizInfo() {
        quizRef.child(String.valueOf(quizId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Quiz quiz = snapshot.getValue(Quiz.class);
                    if (quiz != null) {
                        tvQuizCode.setText("Mã bài: " + quiz.getId());
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Quiz not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(QuizActivity.this, "Error loading quiz data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestions() {
        questionRef.orderByChild("quiz_id").equalTo(quizId)  // Lấy các câu hỏi theo quiz_id
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        questionList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Question question = dataSnapshot.getValue(Question.class);
                            questionList.add(question);
                        }
                        if (!questionList.isEmpty()) {
                            showQuestion(currentQuestionIndex);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(QuizActivity.this, "Error loading questions", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showQuestion(int index) {
        if (index < 0 || index >= questionList.size()) return;

        Question question = questionList.get(index);
        tvQuestion.setText(question.getContent());

        // Load answers for the current question
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
                            currentAnswers.add(answer);
                        }

                        displayAnswers();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(QuizActivity.this, "Error loading answers", Toast.LENGTH_SHORT).show();
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
