package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.R;
import com.example.app_quickquiz.adapter.QuizAdapter;
import com.example.app_quickquiz.model.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchQuizActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ImageView quizImage;
    private TextView tvNoResults;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList;

    private DatabaseReference databaseReference;

    private Button btnGrades, btnSubject, btnChatGpt, mathButton, scienceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ánh xạ view
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerViewQuiz);
        quizImage = findViewById(R.id.quizImage);
        tvNoResults = findViewById(R.id.tvNoResults);

        btnGrades = findViewById(R.id.btnGrades);
        btnSubject = findViewById(R.id.btnSubject);
        btnChatGpt = findViewById(R.id.btnChatGpt);
        mathButton = findViewById(R.id.mathButton);
        scienceButton = findViewById(R.id.scienceButton);

        // Khởi tạo RecyclerView
        quizList = new ArrayList<>();
        //quizAdapter = new QuizAdapter(this, quizList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(quizAdapter);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("quizzes");

        // Xử lý tìm kiếm theo văn bản
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = searchEditText.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    searchQuiz(keyword);
                }
                return true;
            }
            return false;
        });

        // Xử lý tìm kiếm theo nút nhấn
        btnGrades.setOnClickListener(v -> fetchQuizzesByCategory("Subject 1"));
        btnSubject.setOnClickListener(v -> fetchQuizzesByCategory("Subject 2"));
        btnChatGpt.setOnClickListener(v -> fetchQuizzesByCategory("Subject 3"));
        mathButton.setOnClickListener(v -> fetchQuizzesByCategory("Subject 4"));
        scienceButton.setOnClickListener(v -> fetchQuizzesByCategory("Subject 5"));
    }

    // Tìm kiếm theo từ khóa
    private void searchQuiz(String keyword) {
        fetchQuizzesByKeyword(keyword);
    }

    // Lấy danh sách quiz theo từ khóa
    private void fetchQuizzesByKeyword(String keyword) {
        databaseReference.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    Quiz quiz = quizSnapshot.getValue(Quiz.class);
                    if (quiz != null && quiz.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        quizList.add(quiz);
                    }
                }
                quizAdapter.notifyDataSetChanged();
                handleEmptyResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Có thể thêm xử lý lỗi nếu cần
            }
        });
    }

    // Lấy danh sách quiz theo danh mục
    private void fetchQuizzesByCategory(String category) {
        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    Quiz quiz = quizSnapshot.getValue(Quiz.class);
                    if (quiz != null) {
                        quizList.add(quiz);
                    }
                }
                quizAdapter.notifyDataSetChanged();
                handleEmptyResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Kiểm tra nếu không có kết quả thì hiển thị thông báo
    private void handleEmptyResult() {
        if (quizList.isEmpty()) {
            quizImage.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            quizImage.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.GONE);
        }
    }
}
