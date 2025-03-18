package com.example.quickquizapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ClassListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClassAdapter classAdapter;
    private List<ClassItem> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);  // Load layout danh sách lớp

        recyclerView = findViewById(R.id.recyclerViewClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        classList = new ArrayList<>();
        classList.add(new ClassItem("Quản lý dự án CNTT", "Thầy Nguyễn Văn A", R.color.light_blue));
        classList.add(new ClassItem("Tính toán mềm", "Thầy Nguyễn Văn B", R.color.black));
        classList.add(new ClassItem("Phát triển ứng dụng di động", "Thầy Nguyễn Văn Nam", R.color.blue));

        classAdapter = new ClassAdapter(this, classList);
        recyclerView.setAdapter(classAdapter);
    }
}
