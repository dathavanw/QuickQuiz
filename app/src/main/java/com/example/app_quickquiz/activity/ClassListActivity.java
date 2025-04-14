package com.example.app_quickquiz.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.R;
import com.example.app_quickquiz.adapter.ClassAdapter;
import com.example.app_quickquiz.model.ClassModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ClassListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewClasses;
    private ClassAdapter classAdapter;
    private List<ClassModel> classList;
    private DatabaseReference classesRef;
    private TextView tvJoinClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        recyclerViewClasses = findViewById(R.id.recyclerViewClasses);
        tvJoinClass = findViewById(R.id.tvJoinClass);

        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList);
        recyclerViewClasses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClasses.setAdapter(classAdapter);

        classesRef = FirebaseDatabase.getInstance().getReference("classes");

        loadClasses();

        tvJoinClass.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng tham gia lớp học đang được phát triển!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadClasses() {
        classesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot classSnap : snapshot.getChildren()) {
                    ClassModel classModel = classSnap.getValue(ClassModel.class);
                    if (classModel != null) {
                        classList.add(classModel);
                    }
                }
                classAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClassListActivity.this, "Lỗi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
