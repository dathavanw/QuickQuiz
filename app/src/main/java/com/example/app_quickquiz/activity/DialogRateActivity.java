package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;

public class DialogRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Không cần setContentView nếu chỉ hiện dialog
        showRateDialog(); // Gọi trực tiếp khi activity vừa mở
    }

    // Hiển thị dialog đánh giá
    private void showRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_rate, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        ImageView btnTerrible = view.findViewById(R.id.btnTerrible);
        ImageView btnBad = view.findViewById(R.id.btnBad);
        ImageView btnOkey = view.findViewById(R.id.btnOkey);
        ImageView btnGood = view.findViewById(R.id.btnGood);
        ImageView btnGreat = view.findViewById(R.id.btnGreat);
        TextView btnSkip = view.findViewById(R.id.btnSkip);

        setRatingClickListener(dialog, btnTerrible, btnBad, btnOkey, btnGood, btnGreat);
        setRatingClickListener(dialog, btnBad, btnTerrible, btnOkey, btnGood, btnGreat);
        setRatingClickListener(dialog, btnOkey, btnTerrible, btnBad, btnGood, btnGreat);
        setRatingClickListener(dialog, btnGood, btnTerrible, btnBad, btnOkey, btnGreat);
        setRatingClickListener(dialog, btnGreat, btnTerrible, btnBad, btnOkey, btnGood);

        btnSkip.setOnClickListener(v -> {
            dialog.dismiss();
            finish(); // Đóng activity nếu bấm bỏ qua
        });

        dialog.show();
    }

    // Hàm xử lý click và đóng dialog
    private void setRatingClickListener(AlertDialog dialog, ImageView selectedIcon, ImageView... otherIcons) {
        selectedIcon.setOnClickListener(v -> {
            for (ImageView icon : otherIcons) {
                icon.setAlpha(0.3f);
            }
            selectedIcon.setAlpha(1.0f);

            Toast.makeText(DialogRateActivity.this, "Cảm ơn bạn đã đánh giá", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        });
    }
}
