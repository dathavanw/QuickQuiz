package com.example.quickquizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuizCode, tvQuestion;
    private ImageView imgQuestion, btnBack, btnNext, btnPrev;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuizCode = findViewById(R.id.tvQuizCode);
        tvQuestion = findViewById(R.id.tvQuestion);
        imgQuestion = findViewById(R.id.imgQuestion);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);

        // Dữ liệu câu hỏi
        questionList = new ArrayList<>();
        questionList.add(new Question(R.drawable.sample_image, "Pick the places where there is a Disneyland?", new String[]{"France", "California", "China", "Hong Kong"}, 1));
        questionList.add(new Question(R.drawable.unicorn, "Unicorn is the national animal of which country?", new String[]{"Ireland", "Wales", "Scotland", "England"}, 1));

        showQuestion();

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                showQuestion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                showQuestion();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void showQuestion() {
        Question question = questionList.get(currentQuestionIndex);
        imgQuestion.setImageResource(question.getImageResId());
        tvQuestion.setText(question.getQuestionText());
        btnOption1.setText(question.getOptions()[0]);
        btnOption2.setText(question.getOptions()[1]);
        btnOption3.setText(question.getOptions()[2]);
        btnOption4.setText(question.getOptions()[3]);
    }
}
