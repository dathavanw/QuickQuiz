package com.example.btl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FillBlankActivity extends AppCompatActivity {

    private EditText etQuestionContent, etCorrectAnswer;
    private Button btnSave;

    private String quizId, questionId;
    private boolean isQuestionSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_vao_cho_trong);

        etQuestionContent = findViewById(R.id.edtQuestion);
        etCorrectAnswer = findViewById(R.id.edtAnswer);
        btnSave = findViewById(R.id.btnSave);

        quizId = getIntent().getStringExtra("quizId");
        questionId = getIntent().getStringExtra("questionId");

        if (questionId != null && !questionId.isEmpty()) {
            loadQuestionIfEdit();
        }

        btnSave.setOnClickListener(v -> saveQuestion());
    }

    private void loadQuestionIfEdit() {
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions").child(questionId);
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String content = snapshot.child("content").getValue(String.class);
                    etQuestionContent.setText(content);

                    DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("Answers");
                    answerRef.orderByChild("question_id").equalTo(questionId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ansSnap : dataSnapshot.getChildren()) {
                                        String text = ansSnap.child("answer").getValue(String.class);
                                        etCorrectAnswer.setText(text);
                                        break;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(FillBlankActivity.this, "Lỗi khi tải đáp án", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FillBlankActivity.this, "Lỗi khi tải câu hỏi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveQuestion() {
        String content = etQuestionContent.getText().toString().trim();
        String correctAnswer = etCorrectAnswer.getText().toString().trim();

        if (content.isEmpty() || correctAnswer.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập câu hỏi và đáp án đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quizId == null || quizId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy quizId", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isQuestionSaved) return;

        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions");
        DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("Answers");

        DatabaseReference newQuestionRef;
        if (questionId == null || questionId.isEmpty()) {
            newQuestionRef = questionRef.push();
            questionId = newQuestionRef.getKey();
        } else {
            newQuestionRef = questionRef.child(questionId);
        }

        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put("id", questionId);
        questionMap.put("quiz_id", quizId);
        questionMap.put("question_type", "fill_blank");
        questionMap.put("content", content);

        newQuestionRef.setValue(questionMap).addOnSuccessListener(unused -> {
            isQuestionSaved = true;

            answerRef.orderByChild("question_id").equalTo(questionId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                child.getRef().removeValue();
                            }

                            DatabaseReference newAnswerRef = answerRef.push();
                            Map<String, Object> answerMap = new HashMap<>();
                            answerMap.put("id", newAnswerRef.getKey());
                            answerMap.put("question_id", questionId);
                            answerMap.put("answer", correctAnswer);
                            answerMap.put("is_correct", true);
                            newAnswerRef.setValue(answerMap);

                            Intent intent = new Intent(FillBlankActivity.this, AfterAddActivity.class);
                            intent.putExtra("quizId", quizId);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(FillBlankActivity.this, "Lỗi khi xoá đáp án cũ", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public void onBackPressed() {
        if (!isQuestionSaved) saveQuestion();
        super.onBackPressed();
    }
}
