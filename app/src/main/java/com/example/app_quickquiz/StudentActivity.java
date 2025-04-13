package com.example.app_quickquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.activity.HistoryActivity;
import com.example.app_quickquiz.activity.SearchQuizActivity;
import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Quiz;
import com.example.app_quickquiz.repository.QuizRepository;
import com.example.app_quickquiz.sharedpreferences.SharedPreferencesManager;
import com.example.btl.ActivityHoatDong;
import com.example.btl.GiaoVienMainActivity;
import com.example.app_quickquiz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;
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
                    startActivity(new Intent(this, StudentActivity.class));
                    return true;

                case "nav_activity":
                    startActivity(new Intent(this, HistoryActivity.class));
                    return true;
                case "nav_account":
                    startActivity(new Intent(this, AccountActivity.class));
                    return true;
                case "nav_search":
                    startActivity(new Intent(this, SearchQuizActivity.class));
                    return true;
                case "nav_course":

                default:
                    Toast.makeText(this, "Chức năng đang được cập nhật!", Toast.LENGTH_SHORT).show();
                    return true;
            }
        });

        btnSignOut.setOnClickListener(v -> signOut());
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
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Không tìm thấy bài quiz!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

