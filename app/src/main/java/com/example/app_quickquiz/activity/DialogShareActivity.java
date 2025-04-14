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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_quickquiz.R;

public class DialogShareActivity extends AppCompatActivity {

    private static final String SHARE_LINK = "https://www.example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showShareDialog();
    }

    private void showShareDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_share, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        ImageView btnClose = view.findViewById(R.id.btnClose);
        Button btnCopy = view.findViewById(R.id.btnCopy);
        EditText edtLink = view.findViewById(R.id.edtLink);

        edtLink.setText(SHARE_LINK);

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            finish(); // Đóng activity luôn khi tắt dialog
        });

        btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Share link", SHARE_LINK);
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
            finish(); // Đóng sau khi sao chép
        });

        view.findViewById(R.id.btnFacebook).setOnClickListener(v -> shareOnSocialMedia("com.facebook.katana"));
        view.findViewById(R.id.btnInstagram).setOnClickListener(v -> shareOnSocialMedia("com.instagram.android"));
        view.findViewById(R.id.btnMessage).setOnClickListener(v -> shareViaMessage());

        dialog.show();
    }

    private void shareOnSocialMedia(String packageName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, SHARE_LINK);
        intent.setPackage(packageName);
        try {
            startActivity(intent);
            finish(); // Đóng activity sau khi chia sẻ
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareViaMessage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        intent.putExtra("sms_body", SHARE_LINK);
        startActivity(intent);
        finish(); // Đóng activity sau khi chia sẻ
    }
}