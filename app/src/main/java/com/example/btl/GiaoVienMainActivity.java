package com.example.btl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_quickquiz.SignInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GiaoVienMainActivity extends AppCompatActivity {

    private boolean isMenuOpen = false;
    private BottomNavigationView bottomNavigationView;
    private ImageButton menuButton;
    private TextView txtCreateQuiz;

    private LinearLayout recentQuizContainer;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giaovien);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuButton = findViewById(R.id.menu_button);

        if (menuButton != null) {
            menuButton.setOnClickListener(v -> toggleMenu());
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView == null) {
            Log.e("DEBUG", "BottomNavigationView không được include hoặc sai ID!");
        } else {
            Log.d("DEBUG", "BottomNavigationView đã load thành công.");
        }

        txtCreateQuiz = findViewById(R.id.txtCreateQuiz);

        // Ẩn menu ban đầu
        bottomNavigationView.post(() -> {
            bottomNavigationView.setTranslationX(-bottomNavigationView.getWidth());
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        menuButton.setOnClickListener(v -> toggleMenu());

        txtCreateQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(GiaoVienMainActivity.this, AddQuizActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String itemName = getResources().getResourceEntryName(item.getItemId());
            switch (itemName) {
                case "nav_home":
                    return true; // đang ở trang này

                case "nav_activity":
                    startActivity(new Intent(this, ActivityHoatDong.class));
                    return true;
                case "nav_account":
                case "nav_course":
                case "nav_search":
                default:
                    Toast.makeText(this, "Chức năng đang được cập nhật!", Toast.LENGTH_SHORT).show();
                    return true;
            }
        });
        recentQuizContainer = findViewById(R.id.recentQuizContainer);
        txtEmpty = findViewById(R.id.txtEmpty);

        Button btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Đăng xuất Firebase

            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(GiaoVienMainActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xoá backstack
            startActivity(intent);
        });
    }
    private void loadRecentActivities() {
        recentQuizContainer.removeAllViews();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        quizRef.orderByChild("created_by").equalTo(userId)
                .limitToLast(5) // chỉ lấy 5 quiz gần nhất
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            txtEmpty.setVisibility(View.VISIBLE);
                            return;
                        }

                        txtEmpty.setVisibility(View.GONE);
                        for (DataSnapshot quizSnap : snapshot.getChildren()) {
                            String quizTitle = quizSnap.child("title").getValue(String.class);
                            if (quizTitle == null) continue;

                            TextView quizItem = new TextView(GiaoVienMainActivity.this);
                            quizItem.setText("• " + quizTitle);
                            quizItem.setTextSize(16);
                            quizItem.setPadding(0, 12, 0, 12);
                            recentQuizContainer.addView(quizItem);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Lỗi khi tải hoạt động gần đây", error.toException());
                    }
                });
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
    @Override
    protected void onResume() {
        super.onResume();
        loadRecentActivities(); // Tải lại hoạt động mỗi khi quay lại màn hình
    }
}
