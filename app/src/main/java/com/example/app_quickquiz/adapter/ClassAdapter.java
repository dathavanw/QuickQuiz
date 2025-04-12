package com.example.app_quickquiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.app_quickquiz.R;
import com.example.app_quickquiz.model.ClassModel;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassModel> classList;

    public ClassAdapter(List<ClassModel> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassModel classModel = classList.get(position);
        holder.tvClassTitle.setText(classModel.getName());
        holder.tvTeacherName.setText(classModel.getTeacherName());

        // Load ảnh lớp học
        Glide.with(holder.itemView.getContext())
                .load(classModel.getImageUrl())
                .placeholder(R.drawable.ic_class_sample)
                .into(holder.imgClassIcon);
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tvClassTitle, tvTeacherName;
        ImageView imgClassIcon;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassTitle = itemView.findViewById(R.id.tvClassTitle);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            imgClassIcon = itemView.findViewById(R.id.imgClassIcon);
        }
    }
}
