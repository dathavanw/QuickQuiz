package com.example.app_quickquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.adapter.HistoryAdapter;
import com.example.app_quickquiz.model.QuizResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<QuizResult> historyList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Người dùng chưa đăng nhập, chuyển sang trang đăng nhập (SignUpActivity)
            startActivity(new Intent(HistoryActivity.this, SignUpActivity.class));
            finish();
        } else {
            // Người dùng đã đăng nhập, lấy user_id và tải lịch sử
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User_Quiz_Results");

            loadHistory(userId);
        }
    }

    private void loadHistory(String userId) {
        // Lọc kết quả quiz theo user_id của người dùng
        databaseReference.orderByChild("user_id").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        historyList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                QuizResult result = dataSnapshot.getValue(QuizResult.class);
                                historyList.add(result);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Nếu không có lịch sử nào, có thể thông báo cho người dùng
                            Toast.makeText(HistoryActivity.this, "No history found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi khi không thể đọc dữ liệu từ Firebase
                    }
                });
    }
}
