package com.example.app_quickquiz;

import android.os.Bundle;
import android.util.Log;
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

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
   // private QuestionAdapter adapter;
   private QuizRepository quizRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizRepository = new QuizRepository();

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

}
