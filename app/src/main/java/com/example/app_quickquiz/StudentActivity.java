package com.example.app_quickquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Quiz;
import com.example.app_quickquiz.repository.QuizRepository;
import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class StudentActivity extends AppCompatActivity {
    private TextView btnSignOut;
    private EditText txtCodeStartGame;
    private Button btnStartQuiz;
    private ImageView screenstudent;
    AnimationDrawable animationDrawable;
    private QuizRepository quizRepository;
    private SharedPreferencesManager sharedPreferencesManager;
    private FirebaseAuth mAuth;
    private String userId1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnSignOut = findViewById(R.id.tvSignOut);



        // userID được truyền từ màn hình đăng nhập sang
//        userId1 = getIntent().getStringExtra("userId");
//        Log.d("USER_ID Ở MÀN HÌNH CHÍNH", "Giá Trị: " + userId1);
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId1 = preferences.getString("userId", null);
        Log.d("USER_ID TỪ SHARED_PREFERENCES", "Giá Trị: " + userId1);



        txtCodeStartGame = findViewById(R.id.txtCodeStartGame);
        btnStartQuiz =findViewById(R.id.btnStartGame);
        btnStartQuiz.setOnClickListener(v->checkQuizExistsById());
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

    public void checkQuizExistsById(){
      String codeQuiz = txtCodeStartGame.getText().toString().trim();
        if (codeQuiz.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã bài quiz", Toast.LENGTH_SHORT).show();

        }else {
          //  Toast.makeText(this, "Mã quiz phải là số", Toast.LENGTH_SHORT).show();
        }
        quizRepository = new QuizRepository();
        quizRepository.checkQuizExistsById(codeQuiz, (exists ,snapshot)->{
            if(exists){
                for (DataSnapshot quizSnap : snapshot.getChildren()) {
//                    String time_limit = quizSnap.child("title").getValue(String.class);
                    Quiz quiz = quizSnap.getValue(Quiz.class);
                    //Toast.makeText(this, "Tìm thấy quiz: " + title, Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình làm bài
                    Intent intent = new Intent(StudentActivity.this, QuizActivity.class);
                    intent.putExtra("quizId", codeQuiz);
                    intent.putExtra("timeLimit", quiz.getTime_limit());
                 //   intent.putExtra("userId2", userId1);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Không tìm thấy bài quiz!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

