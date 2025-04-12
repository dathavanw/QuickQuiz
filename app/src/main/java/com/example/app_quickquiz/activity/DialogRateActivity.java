package com.example.app_quickquiz.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;

public class DialogRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sửa lại tên layout có chứa Rate
//        setContentView(R.layout.activity_main);
//
//        // Giả sử bạn có một nút để mở dialog rate
//        findViewById(R.id.btnShowRateDialog).setOnClickListener(view -> showRateDialog());
    }

    // Hiển thị dialog đánh giá
    private void showRateDialog() {
        // Tạo AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout của dialog rate
        View view = getLayoutInflater().inflate(R.layout.dialog_rate, null);

        // Thiết lập cho dialog
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Lấy các biểu tượng đánh giá và nút skip
        ImageView btnTerrible = view.findViewById(R.id.btnTerrible);
        ImageView btnBad = view.findViewById(R.id.btnBad);
        ImageView btnOkey = view.findViewById(R.id.btnOkey);
        ImageView btnGood = view.findViewById(R.id.btnGood);
        ImageView btnGreat = view.findViewById(R.id.btnGreat);
        TextView btnSkip = view.findViewById(R.id.btnSkip);

        // Thiết lập các sự kiện cho các biểu tượng đánh giá
        setRatingClickListener(btnTerrible, btnBad, btnOkey, btnGood, btnGreat);
        setRatingClickListener(btnBad, btnTerrible, btnOkey, btnGood, btnGreat);
        setRatingClickListener(btnOkey, btnTerrible, btnBad, btnGood, btnGreat);
        setRatingClickListener(btnGood, btnTerrible, btnBad, btnOkey, btnGreat);
        setRatingClickListener(btnGreat, btnTerrible, btnBad, btnOkey, btnGood);

        // Sự kiện skip
        btnSkip.setOnClickListener(v -> dialog.dismiss());

        // Hiển thị dialog
        dialog.show();
    }

    // Hàm xử lý sự kiện khi người dùng chọn biểu tượng đánh giá
    private void setRatingClickListener(ImageView selectedIcon, ImageView... otherIcons) {
        selectedIcon.setOnClickListener(v -> {
            // Làm mờ tất cả các biểu tượng khác
            for (ImageView icon : otherIcons) {
                icon.setAlpha(0.3f);  // Đặt độ mờ của các biểu tượng khác
            }
            // Làm đậm biểu tượng đã chọn
            selectedIcon.setAlpha(1.0f);  // Đặt độ mờ của biểu tượng đã chọn
        });
    }
}
