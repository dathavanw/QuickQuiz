package com.example.quickquizapp.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickquizapp.R;
import com.example.quickquizapp.UserQuizResult;
import com.example.quickquizapp.adapter.HoatDongAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class HoatDongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoatDongAdapter adapter;
    private ArrayList<UserQuizResult> quizResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoat_dong);

        recyclerView = findViewById(R.id.recyclerViewHoatDong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizResultList = new ArrayList<>();
        adapter = new HoatDongAdapter(this, quizResultList);
        recyclerView.setAdapter(adapter);

        loadQuizResults();
    }

    private void loadQuizResults() {
        FirebaseDatabase.getInstance().getReference("user_quiz_results")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        quizResultList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            UserQuizResult result = data.getValue(UserQuizResult.class);
                            if (result != null) {
                                quizResultList.add(result);
                            }
                        }

                        // Sắp xếp giảm dần theo thời gian
                        Collections.sort(quizResultList, new Comparator<UserQuizResult>() {
                            @Override
                            public int compare(UserQuizResult o1, UserQuizResult o2) {
                                return parseDate(o2.getTaken_at()).compareTo(parseDate(o1.getTaken_at()));
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HoatDongActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm hỗ trợ chuyển chuỗi thời gian thành Date
    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateStr);
        } catch (ParseException e) {
            return new Date(0); // Nếu lỗi, trả về thời gian mặc định
        }
    }
}
