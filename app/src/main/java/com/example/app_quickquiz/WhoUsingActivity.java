package com.example.app_quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class WhoUsingActivity extends AppCompatActivity {

    LinearLayout linearTeacher, linearStudent;
    String selectedRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_using);

        linearTeacher = findViewById(R.id.teacherLayout);
        linearStudent = findViewById(R.id.studentLayout);

        linearStudent.setOnClickListener(v -> {
            selectedRole = "student";
            highlightRole();
            getRoleToSignUp(selectedRole);
        });

        linearTeacher.setOnClickListener(v -> {
            selectedRole = "teacher";
            highlightRole();
            getRoleToSignUp(selectedRole);
        });


    }

    // Thay đổi background theo lựa chọn
    private void highlightRole() {
        if (selectedRole.equals("student")) {
            linearStudent.setBackgroundResource(R.drawable.bg_selected_role);
            linearTeacher.setBackgroundResource(R.drawable.round_background);
        } else {
            linearTeacher.setBackgroundResource(R.drawable.bg_selected_role);
            linearStudent.setBackgroundResource(R.drawable.round_background);
        }
    }

    private void getRoleToSignUp(String role){
        Intent intent = new Intent(WhoUsingActivity.this, SignUpActivity.class);
        intent.putExtra("role",role);
        startActivity(intent);
    }

}
