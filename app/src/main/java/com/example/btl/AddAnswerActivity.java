package com.example.btl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddAnswerActivity extends AppCompatActivity {

    private EditText etAnswer;
    private CheckBox cbCorrectAnswer;
    private Button btnSave;

    private String quizId;
    private String questionId;
    private int answerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);

        etAnswer = findViewById(R.id.etAnswer);
        cbCorrectAnswer = findViewById(R.id.cbCorrectAnswer);
        btnSave = findViewById(R.id.btnSave);

        quizId = getIntent().getStringExtra("quizId");
        questionId = getIntent().getStringExtra("questionId");
        answerNumber = getIntent().getIntExtra("answer_number", -1);

        btnSave.setOnClickListener(v -> saveAnswer());
    }

    private void saveAnswer() {
        String answerText = etAnswer.getText().toString().trim();
        boolean isCorrect = cbCorrectAnswer.isChecked();

        if (answerText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung câu trả lời!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference answersRef = FirebaseDatabase.getInstance().getReference("Answers");

        answersRef.get().addOnSuccessListener(snapshot -> {
            long count = snapshot.getChildrenCount();
            int nextId = (int) count;

            // Tạo một node mới để lấy key (firebase_id)
            DatabaseReference newAnswerRef = answersRef.push();
            String firebaseId = newAnswerRef.getKey();  // ID tự sinh bởi Firebase

            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put("id", nextId);
            answerMap.put("firebase_id", firebaseId);  // 👉 thêm trường firebase_id
            answerMap.put("content", answerText);
            answerMap.put("question_id", Integer.parseInt(questionId));
            answerMap.put("is_correct", isCorrect);

            newAnswerRef.setValue(answerMap)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Đã lưu câu trả lời!", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("answer_number", answerNumber);
                        resultIntent.putExtra("answer_text", answerText);
                        resultIntent.putExtra("firebase_id", firebaseId);  // Truyền về nếu cần
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
