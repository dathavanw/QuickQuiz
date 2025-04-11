package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.repository.FeedBackRepository;

public class SupportActivity extends AppCompatActivity {
    private Button btnSendMessage;
    private EditText txtMessages, txtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        txtEmail = findViewById(R.id.txtEmailFeedback);
        txtMessages = findViewById(R.id.txtMessage);

        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(v -> sendMessage());
    }

    public void sendMessage() {
        String emailFeedback = txtEmail.getText().toString().trim();
        String message = txtMessages.getText().toString().trim();
        if (!emailFeedback.isEmpty() && !message.isEmpty()) {
            FeedBackRepository feedbackRepository = new FeedBackRepository();
            feedbackRepository.sendFeedback(emailFeedback, message, isSuccess -> {
                if (isSuccess) {
                    // Chuyển sang màn hình thông báo thành công
                    startActivity(new Intent(SupportActivity.this, SupportCompleteActivity.class));
                } else {
                    Toast.makeText(SupportActivity.this, "Gửi thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SupportActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }



}






