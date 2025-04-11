package com.example.quickquizapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RateDialog extends Dialog {

    private ImageView btnTerrible, btnBad, btnOkey, btnGood, btnGreat;
    private TextView btnSkip;
    private int selectedRating = -1;

    public RateDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_rate);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Làm nền trong suốt

        // Áp dụng hiệu ứng mở hộp thoại
        View dialogView = findViewById(R.id.dialogContainer);
        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        dialogView.startAnimation(slideUp);

        // Ánh xạ các view
        btnTerrible = findViewById(R.id.btnTerrible);
        btnBad = findViewById(R.id.btnBad);
        btnOkey = findViewById(R.id.btnOkey);
        btnGood = findViewById(R.id.btnGood);
        btnGreat = findViewById(R.id.btnGreat);
        btnSkip = findViewById(R.id.btnSkip);

        btnTerrible.setOnClickListener(v -> selectRating(0));
        btnBad.setOnClickListener(v -> selectRating(1));
        btnOkey.setOnClickListener(v -> selectRating(2));
        btnGood.setOnClickListener(v -> selectRating(3));
        btnGreat.setOnClickListener(v -> selectRating(4));

        // Xử lý khi nhấn Skip
        btnSkip.setOnClickListener(v -> {
            Toast.makeText(context, "You skipped rating!", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    private void selectRating(int rating) {
        selectedRating = rating;

        resetIcons(); // Reset tất cả về trạng thái mờ

        if (rating == 0) {
            btnTerrible.setAlpha(1.0f);
        } else if (rating == 1) {
            btnBad.setAlpha(1.0f);
        } else if (rating == 2) {
            btnOkey.setAlpha(1.0f);
        } else if (rating == 3) {
            btnGood.setAlpha(1.0f);
        } else if (rating == 4) {
            btnGreat.setAlpha(1.0f);
        }
    }

    private void resetIcons() {
        btnTerrible.setAlpha(0.3f);
        btnBad.setAlpha(0.3f);
        btnOkey.setAlpha(0.3f);
        btnGood.setAlpha(0.3f);
        btnGreat.setAlpha(0.3f);
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
