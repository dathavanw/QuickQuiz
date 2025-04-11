package com.example.app_quickquiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.adapter.QuestionAdapter;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu câu hỏi và đáp án từ Firebase hoặc JSON
        List<Question> questionList = loadQuestions();
        Map<Integer, List<Answer>> answersMap = loadAnswers();

        // Khởi tạo Adapter
        adapter = new QuestionAdapter(questionList, answersMap);
        recyclerView.setAdapter(adapter);
    }

    // Các phương thức giả lập để lấy dữ liệu
    private List<Question> loadQuestions() {
        // Giả lập dữ liệu câu hỏi
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, 1, "What is the capital of France?", "multiple_choice"));
        questions.add(new Question(2, 1, "What is 2 + 2?", "multiple_choice"));
        return questions;
    }

    private Map<Integer, List<Answer>> loadAnswers() {
        // Giả lập dữ liệu đáp án
        Map<Integer, List<Answer>> answersMap = new HashMap<>();

        List<Answer> answersForQ1 = new ArrayList<>();
        answersForQ1.add(new Answer(1, 1, "Paris", true));
        answersForQ1.add(new Answer(2, 1, "London", false));
        answersMap.put(1, answersForQ1);

        List<Answer> answersForQ2 = new ArrayList<>();
        answersForQ2.add(new Answer(3, 2, "3", false));
        answersForQ2.add(new Answer(4, 2, "4", true));
        answersMap.put(2, answersForQ2);

        return answersMap;
    }
}
