package com.example.quickquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HoatDongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoatDongAdapter adapter;
    private List<HoatDongModel> hoatDongList;
    private TextView signOutText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoat_dong);

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.recyclerViewHoatDong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách dữ liệu mẫu
        hoatDongList = new ArrayList<>();
        hoatDongList.add(new HoatDongModel("14:00 3/2/2025", "84435", "25/30"));
        hoatDongList.add(new HoatDongModel("07:35 4/1/2025", "81967", "36/45"));
        hoatDongList.add(new HoatDongModel("17:00 3/1/2024", "82245", "24/25"));

        // Khởi tạo Adapter và gán vào RecyclerView
        adapter = new HoatDongAdapter(hoatDongList, this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    // Xử lý khi bấm vào nút "Xem chi tiết"
    private void onItemClick(HoatDongModel item) {
        Intent intent = new Intent(this, ChiTietBaiKiemTraActivity.class);
        intent.putExtra("maBai", item.getMaBai());
        intent.putExtra("diem", item.getDiem());
        startActivity(intent);
    }
}
