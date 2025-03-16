package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SupportCompleteActivity extends AppCompatActivity {

    private Button btnCompleteMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_complete);

        btnCompleteMessage = findViewById(R.id.btnCompleteMessage);
        btnCompleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupportCompleteActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
