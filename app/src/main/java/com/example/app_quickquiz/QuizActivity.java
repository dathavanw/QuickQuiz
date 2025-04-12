package com.example.app_quickquiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.adapter.QuestionWithAnswersAdapter;
import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.QuestionWithAnswers;
import com.example.app_quickquiz.repository.QuizRepository;

import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
   // private QuestionAdapter adapter;
   private QuizRepository quizRepository;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizRepository = new QuizRepository();
        timerTextView = findViewById(R.id.timerTextView);
        recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String quiz_id = getIntent().getStringExtra("quizId");
        int time_limit = getIntent().getIntExtra("timeLimit", 0);

        Log.d("QuizActivity", "Quiz ID: " + quiz_id);
        Log.d("QuizActivity", "Time Limit: " + time_limit);


        // Gọi dữ liệu
        quizRepository.getQuestionsWithAnswersByQuizId(quiz_id, new Database.OnGetDataListener<List<QuestionWithAnswers>>() {
            @Override
            public void onSuccess(List<QuestionWithAnswers> questionWithAnswersList) {
                QuestionWithAnswersAdapter adapter = new QuestionWithAnswersAdapter(QuizActivity.this, questionWithAnswersList);
                recyclerView.setAdapter(adapter);
                startCountdown(time_limit);  // Bắt đầu đếm ngược
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(QuizActivity.this, "Lỗi khi tải câu hỏi", Toast.LENGTH_SHORT).show();
            }
        });









//        quizRepository = new QuizRepository();
//        String matest = "1744454229128";
//        quizRepository.fetchQuestionsAndAnswers(matest, new Database.OnGetDataListener<List<QuestionWithAnswers>>() {
//            @Override
//            public void onSuccess(List<QuestionWithAnswers> data) {
//                // Hiển thị danh sách câu hỏi và đáp án
//                for (QuestionWithAnswers item : data) {
//                    Log.d("Quiz", "Question: " + item.getQuestion().getContent());
//                    for (Answer answer : item.getAnswers()) {
//                        Log.d("Quiz", "Answer: " + answer.getAnswer());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(QuizActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });






    }


    private void startCountdown(int timeLimitInMinutes) {
        timerTextView = findViewById(R.id.timerTextView);
        long totalTimeInMillis = timeLimitInMinutes * 60 * 1000;

        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
                // TODO: Nộp bài tự động hoặc hiện dialog báo hết giờ
              //  submitQuizAutomatically();
            }
        };
        countDownTimer.start();
    }

}
