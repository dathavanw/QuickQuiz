package com.example.app_quickquiz.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;

public class DialogShareActivity extends AppCompatActivity {

    private static final String SHARE_LINK = "https://www.example.com";  // Đường dẫn link sẽ được chia sẻ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);  // Sửa lại thành layout hiển thị Share
//
//        // Giả sử bạn có một nút để mở dialog share
//        findViewById(R.id.btnShowShareDialog).setOnClickListener(view -> showShareDialog());
    }

    // Hiển thị dialog chia sẻ
    private void showShareDialog() {
        // Tạo AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout của dialog share
        View view = getLayoutInflater().inflate(R.layout.dialog_share, null);

        // Thiết lập cho dialog
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Thiết lập các nút và hành động trong dialog
        ImageView btnClose = view.findViewById(R.id.btnClose);
        Button btnCopy = view.findViewById(R.id.btnCopy);
        EditText edtLink = view.findViewById(R.id.edtLink);

        // Điền link mặc định vào EditText
        edtLink.setText(SHARE_LINK);

        // Sự kiện đóng dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Sự kiện sao chép liên kết
        btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Share link", SHARE_LINK);
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();  // Đóng dialog sau khi sao chép
        });

        // Sự kiện chia sẻ qua Facebook
        view.findViewById(R.id.btnFacebook).setOnClickListener(v -> shareOnSocialMedia("com.facebook.katana"));

        // Sự kiện chia sẻ qua Instagram
        view.findViewById(R.id.btnInstagram).setOnClickListener(v -> shareOnSocialMedia("com.instagram.android"));

        // Sự kiện chia sẻ qua Message
        view.findViewById(R.id.btnMessage).setOnClickListener(v -> shareViaMessage());

        // Hiển thị dialog
        dialog.show();
    }

    // Chia sẻ qua các mạng xã hội (Facebook, Instagram, v.v.)
    private void shareOnSocialMedia(String packageName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, SHARE_LINK);
        intent.setPackage(packageName);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Chia sẻ qua SMS (Message)
    private void shareViaMessage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        intent.putExtra("sms_body", SHARE_LINK);
        startActivity(intent);
    }
}
