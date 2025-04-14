package com.example.app_quickquiz.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.AccountActivity;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.StudentActivity;
import com.example.app_quickquiz.adapter.HistoryAdapter;
import com.example.app_quickquiz.model.QuizResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<QuizResult> historyList;
    private DatabaseReference databaseReference;
    private boolean isMenuOpen = false;
    private ImageButton menuButton;
    private BottomNavigationView bottomNavigationView;
    private String userId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId1 = preferences.getString("userId", null);
        Log.d("USER_ID TỪ SHARED_PREFERENCES", "Giá Trị: " + userId1);



        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("User_Quiz_Results");
        menuButton = findViewById(R.id.menu_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_activity);

        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });

        menuButton.setOnClickListener(v -> toggleMenu());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String itemName = getResources().getResourceEntryName(item.getItemId());
            switch (itemName) {
                case "nav_home":
                    startActivity(new Intent(this, StudentActivity.class));
                    return true;

                case "nav_activity":
                    startActivity(new Intent(this, HistoryActivity.class));
                    return true;
                case "nav_account":
                    startActivity(new Intent(this, AccountActivity.class));
                    return true;
                case "nav_search":
                    startActivity(new Intent(this, SearchQuizActivity.class));
                    return true;
                case "nav_course":

                default:
                    Toast.makeText(this, "Chức năng đang được cập nhật!", Toast.LENGTH_SHORT).show();
                    return true;
            }
        });

        loadHistory();
    }
    private void toggleMenu() {
        ObjectAnimator animatorX;
        ObjectAnimator animatorAlpha;

        if (isMenuOpen) {
            animatorX = ObjectAnimator.ofFloat(bottomNavigationView, "translationX", -bottomNavigationView.getWidth());
            animatorAlpha = ObjectAnimator.ofFloat(bottomNavigationView, "alpha", 1f, 0f);
            animatorX.setDuration(300);
            animatorAlpha.setDuration(200);
            animatorX.start();
            animatorAlpha.start();
            animatorX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bottomNavigationView.setVisibility(BottomNavigationView.GONE);
                }
            });
        } else {
            bottomNavigationView.setVisibility(BottomNavigationView.VISIBLE);
            animatorX = ObjectAnimator.ofFloat(bottomNavigationView, "translationX", 0f);
            animatorAlpha = ObjectAnimator.ofFloat(bottomNavigationView, "alpha", 0f, 1f);
            animatorX.setDuration(300);
            animatorAlpha.setDuration(200);
            animatorX.start();
            animatorAlpha.start();
        }
        isMenuOpen = !isMenuOpen;
    }
    private void loadHistory() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.orderByChild("user_id").equalTo(userId1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        historyList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            QuizResult result = dataSnapshot.getValue(QuizResult.class);
                            historyList.add(result);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "loadHistory: " + error.getMessage());
                    }
                });
    }

}