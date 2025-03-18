package com.example.quickquizapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChiTietBaiKiemTraActivity extends AppCompatActivity {

    private TextView txtMaBai, txtDiem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);

        txtMaBai = findViewById(R.id.txtMaBai);
        txtDiem = findViewById(R.id.txtDiem);

        // Nhận dữ liệu từ Intent
        String maBai = getIntent().getStringExtra("maBai");
        String diem = getIntent().getStringExtra("diem");

        txtMaBai.setText("Mã bài: " + maBai);
        txtDiem.setText("Điểm: " + diem);
    }
}
