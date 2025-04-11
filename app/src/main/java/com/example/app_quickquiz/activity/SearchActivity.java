package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.R;
import com.example.app_quickquiz.adapter.QuizAdapter;
import com.example.app_quickquiz.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<Quiz> allQuizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerViewQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allQuizzes = new ArrayList<>();
        quizAdapter = new QuizAdapter(allQuizzes);
        recyclerView.setAdapter(quizAdapter);

        loadAllQuizzesFromFirebase();
        setupCategoryButtons();
        setupSearchFunction();
    }

    private void loadAllQuizzesFromFirebase() {
        FirebaseDatabase.getInstance().getReference("quizzes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allQuizzes.clear();
                        for (DataSnapshot quizSnap : snapshot.getChildren()) {
                            Quiz quiz = quizSnap.getValue(Quiz.class);
                            if (quiz != null) {
                                allQuizzes.add(quiz);
                            }
                        }
                        quizAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSearchFunction() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterQuiz(s.toString());
            }
        });
    }

    private void filterQuiz(String keyword) {
        List<Quiz> filteredList = new ArrayList<>();
        for (Quiz quiz : allQuizzes) {
            if (quiz.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(quiz);
            }
        }
        quizAdapter.updateList(filteredList);
    }

    private void setupCategoryButtons() {
        LinearLayout categoryLayout = findViewById(R.id.categoryLayout);

        FirebaseDatabase.getInstance().getReference("categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot catSnap : snapshot.getChildren()) {
                            String categoryName = catSnap.child("name").getValue(String.class);
                            if (categoryName != null) {
                                Button button = new Button(SearchActivity.this);
                                button.setText(categoryName);
                                button.setOnClickListener(v -> filterByCategory(categoryName));
                                categoryLayout.addView(button);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void filterByCategory(String categoryName) {
        FirebaseDatabase.getInstance().getReference("categories")
                .orderByChild("name").equalTo(categoryName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot catSnap : snapshot.getChildren()) {
                            Long categoryId = catSnap.child("id").getValue(Long.class); // <-- lấy ID của category
                            if (categoryId != null) {
                                List<Quiz> filtered = new ArrayList<>();
                                for (Quiz quiz : allQuizzes) {
                                    if (quiz.getCategoryId() == categoryId.intValue()) {
                                        filtered.add(quiz);
                                    }
                                }
                                quizAdapter.updateList(filtered);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}
