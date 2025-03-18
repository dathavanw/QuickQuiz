package com.example.quickquizapp;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Xử lý nút quay lại
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Tạo danh sách thông báo
        List<NotificationItem> notifications = new ArrayList<>();
        notifications.add(new NotificationItem(R.drawable.ic_update, "02/04/25 - 7:30PM", "New update is available. Upgrade now for the best experience."));
        notifications.add(new NotificationItem(R.drawable.ic_success, "02/03/25 - 6:30PM", "Your account has been successfully created."));

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NotificationAdapter(notifications));
    }
}
