package com.example.app_quickquiz;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.repository.QuizRepository;
import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;

public class StudentActivity extends AppCompatActivity {
    private TextView btnSignOut;
    private EditText txtCodeStartGame;
    private Button btnStartQuiz;
    private ImageView screenstudent;
    AnimationDrawable animationDrawable;
    private QuizRepository quizRepository;
    private SharedPreferencesManager sharedPreferencesManager;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnSignOut = findViewById(R.id.tvSignOut);

        txtCodeStartGame = findViewById(R.id.txtCodeStartGame);
        btnStartQuiz =findViewById(R.id.btnStartGame);
        btnStartQuiz.setOnClickListener(v->getQuiz());
        screenstudent = findViewById(R.id.screenstudent);
        screenstudent.setBackgroundResource(R.drawable.animation_student_main);

        animationDrawable = (AnimationDrawable) screenstudent.getBackground();
        animationDrawable.start();

        mAuth = FirebaseAuth.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(this);
        btnSignOut.setOnClickListener(v -> signOut());
    }

    public void signOut(){
        mAuth.signOut();
        sharedPreferencesManager.clearLoginCredentials();
        Intent intent = new Intent(this,OnboardingActivity.class);
        startActivity(intent);
    }

    public void getQuiz(){
//        String codeQuiz = txtCodeStartGame.getText().toString().trim();
//        if(!codeQuiz.isEmpty()){
//            quizRepository = new QuizRepository();
//            quizRepository.getQuiz(codeQuiz, quiz -> {
//                if (quiz != null) {
//                    // Chuyển đến giao diện quiz
//                    Intent intent = new Intent(StudentActivity.this, QuizActivity.class);
//                //    intent.putExtra("quizId", quiz.getId());
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(StudentActivity.this, "Mã bài kiểm tra không hợp lệ!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

    }
}

