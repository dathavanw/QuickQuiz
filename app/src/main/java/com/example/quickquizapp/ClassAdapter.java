package com.example.quickquizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Context context;
    private List<ClassItem> classList;

    public ClassAdapter(Context context, List<ClassItem> classList) {
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassItem classItem = classList.get(position);
        holder.tvClassName.setText(classItem.getClassName());
        holder.tvTeacherName.setText(classItem.getTeacherName());
        holder.cardView.setCardBackgroundColor(context.getResources().getColor(classItem.getBackgroundColor()));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tvClassName, tvTeacherName;
        CardView cardView;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tvClassTitle);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            //cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
