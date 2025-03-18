package com.example.quickquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private ImageView quizImage;
    private TextView signOutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        quizImage = findViewById(R.id.quizImage);
        //signOutText = findViewById(R.id.signOutText); // Sửa lỗi ở đây

        // Xử lý sự kiện nhấn vào "Sign out"
        signOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về màn hình đăng nhập hoặc thoát ứng dụng
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện khi người dùng nhập tìm kiếm (có thể thêm xử lý API sau này)
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = searchEditText.getText().toString().trim();
            if (!searchText.isEmpty()) {
                // Hiển thị hình ảnh hoặc thay đổi nội dung theo kết quả tìm kiếm
                quizImage.setImageResource(R.drawable.ic_quiz_search); // ic_re
            }
            return false;
        });
    }
}
