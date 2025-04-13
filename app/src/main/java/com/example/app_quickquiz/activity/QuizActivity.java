package com.example.app_quickquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;
import com.example.app_quickquiz.model.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvQuizCode;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private ImageView btnPrev, btnNext, btnBack;

    private List<Question> questionList = new ArrayList<>();
    private List<Answer> answerList = new ArrayList<>();

    private int currentQuestionIndex = 0;
    private int quizId;

    private DatabaseReference quizRef, questionRef, answerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();
        setupFirebase();

        quizId = getIntent().getIntExtra("QUIZ_ID", -1);
        if (quizId == -1) {
            Toast.makeText(this, "Quiz ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadQuizInfo();
        loadQuestions();

        btnBack.setOnClickListener(v -> onBackPressed());
        btnPrev.setOnClickListener(v -> showPreviousQuestion());
        btnNext.setOnClickListener(v -> showNextQuestion());
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuizCode = findViewById(R.id.tvQuizCode);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupFirebase() {
        quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        questionRef = FirebaseDatabase.getInstance().getReference("Questions");
        answerRef = FirebaseDatabase.getInstance().getReference("Answers");
    }

    private void loadQuizInfo() {
        quizRef.child(String.valueOf(quizId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Quiz quiz = snapshot.getValue(Quiz.class);
                if (quiz != null) {
                    tvQuizCode.setText("Mã bài: " + quiz.getId());
                } else {
                    Toast.makeText(QuizActivity.this, "Quiz not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizActivity.this, "Failed to load quiz info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestions() {
        questionRef.orderByChild("quiz_id").equalTo(quizId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        questionList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Question question = ds.getValue(Question.class);
                            if (question != null) questionList.add(question);
                        }
                        if (!questionList.isEmpty()) {
                            showQuestion(currentQuestionIndex);
                        } else {
                            Toast.makeText(QuizActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuizActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
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
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        answerList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Answer answer = ds.getValue(Answer.class);
                            if (answer != null) answerList.add(answer);
                        }
                        displayAnswers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuizActivity.this, "Failed to load answers", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayAnswers() {
        Button[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.VISIBLE);
            if (i < answerList.size()) {
                Answer answer = answerList.get(i);
                buttons[i].setText(answer.getContent());
                buttons[i].setTypeface(null, answer.isIs_correct() ?
                        android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
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
