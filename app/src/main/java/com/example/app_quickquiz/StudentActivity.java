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

import com.example.app_quickquiz.database.Database;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnSignOut = findViewById(R.id.tvSignOut);

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
        }
        int idQuiz;
        try {
            idQuiz = Integer.parseInt(codeQuiz);
        }catch(NumberFormatException e) {
            Toast.makeText(this, "Mã quiz phải là số", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu xảy ra lỗi
        }
        Database db = new Database();
        db.checkQuizExistsById(idQuiz, (exists ,snapshot)->{
            if(exists){
                for (DataSnapshot quizSnap : snapshot.getChildren()) {
                    String title = quizSnap.child("title").getValue(String.class);
                    Toast.makeText(this, "Tìm thấy quiz: " + title, Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình làm bài nếu cần
                    // Intent intent = new Intent(this, QuizActivity.class);
                    // intent.putExtra("quizId", quizId);
                    // startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Không tìm thấy bài quiz!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

