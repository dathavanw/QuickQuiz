package com.example.btl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddMultipleChoiceActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText etQuestionContent, etOption1, etOption2, etOption3, etOption4;
    private RadioButton cbOption1, cbOption2, cbOption3, cbOption4;

    private String quizId, questionId;
    private boolean isQuestionSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhieu_lua_chon);

        quizId = getIntent().getStringExtra("quizId");
        questionId = getIntent().getStringExtra("questionId");

        etQuestionContent = findViewById(R.id.etQuestion);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);
        etOption4 = findViewById(R.id.etOption4);

        cbOption1 = findViewById(R.id.cbOption1);
        cbOption2 = findViewById(R.id.cbOption2);
        cbOption3 = findViewById(R.id.cbOption3);
        cbOption4 = findViewById(R.id.cbOption4);

        btnSave = findViewById(R.id.btnSave);

        cbOption1.setOnClickListener(v -> selectOnly(cbOption1));
        cbOption2.setOnClickListener(v -> selectOnly(cbOption2));
        cbOption3.setOnClickListener(v -> selectOnly(cbOption3));
        cbOption4.setOnClickListener(v -> selectOnly(cbOption4));

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
                                    int index = 0;
                                    for (DataSnapshot ansSnap : dataSnapshot.getChildren()) {
                                        String text = ansSnap.child("answer").getValue(String.class);
                                        Boolean isCorrect = ansSnap.child("is_correct").getValue(Boolean.class);

                                        switch (index) {
                                            case 0:
                                                etOption1.setText(text);
                                                cbOption1.setChecked(Boolean.TRUE.equals(isCorrect));
                                                break;
                                            case 1:
                                                etOption2.setText(text);
                                                cbOption2.setChecked(Boolean.TRUE.equals(isCorrect));
                                                break;
                                            case 2:
                                                etOption3.setText(text);
                                                cbOption3.setChecked(Boolean.TRUE.equals(isCorrect));
                                                break;
                                            case 3:
                                                etOption4.setText(text);
                                                cbOption4.setChecked(Boolean.TRUE.equals(isCorrect));
                                                break;
                                        }
                                        index++;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(AddMultipleChoiceActivity.this, "Lỗi khi tải đáp án", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AddMultipleChoiceActivity.this, "Lỗi khi tải câu hỏi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectOnly(RadioButton selected) {
        cbOption1.setChecked(false);
        cbOption2.setChecked(false);
        cbOption3.setChecked(false);
        cbOption4.setChecked(false);
        selected.setChecked(true);
    }

    private void saveQuestion() {
        String content = etQuestionContent.getText().toString().trim();
        String answer1 = etOption1.getText().toString().trim();
        String answer2 = etOption2.getText().toString().trim();
        String answer3 = etOption3.getText().toString().trim();
        String answer4 = etOption4.getText().toString().trim();

        if (content.isEmpty() || answer1.isEmpty() || answer2.isEmpty()
                || answer3.isEmpty() || answer4.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ câu hỏi và 4 đáp án!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbOption1.isChecked() && !cbOption2.isChecked()
                && !cbOption3.isChecked() && !cbOption4.isChecked()) {
            Toast.makeText(this, "Vui lòng chọn một đáp án đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quizId == null || quizId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy quizId, không thể lưu câu hỏi!", Toast.LENGTH_LONG).show();
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
        questionMap.put("question_type", "multiple_choice");
        questionMap.put("content", content);

        newQuestionRef.setValue(questionMap).addOnSuccessListener(unused -> {
            isQuestionSaved = true;
            Toast.makeText(this, "Đã lưu câu hỏi!", Toast.LENGTH_SHORT).show();

            answerRef.orderByChild("question_id").equalTo(questionId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                child.getRef().removeValue();
                            }

                            saveAnswer(answerRef, answer1, cbOption1.isChecked());
                            saveAnswer(answerRef, answer2, cbOption2.isChecked());
                            saveAnswer(answerRef, answer3, cbOption3.isChecked());
                            saveAnswer(answerRef, answer4, cbOption4.isChecked());

                            Intent intent = new Intent(AddMultipleChoiceActivity.this, AfterAddActivity.class);
                            intent.putExtra("quizId", quizId);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(AddMultipleChoiceActivity.this, "Lỗi khi xoá đáp án cũ", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void saveAnswer(DatabaseReference ref, String text, boolean isCorrect) {
        DatabaseReference newAnswerRef = ref.push();
        String answerId = newAnswerRef.getKey();

        Map<String, Object> ans = new HashMap<>();
        ans.put("id", answerId);
        ans.put("question_id", questionId);
        ans.put("answer", text);
        ans.put("is_correct", isCorrect);

        newAnswerRef.setValue(ans);
    }

    @Override
    public void onBackPressed() {
        if (!isQuestionSaved) {
            saveQuestion();
        }
        super.onBackPressed();
    }
}
