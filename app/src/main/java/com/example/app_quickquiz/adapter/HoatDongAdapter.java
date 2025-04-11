package com.example.quickquizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickquizapp.R;
import com.example.quickquizapp.UserQuizResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HoatDongAdapter extends RecyclerView.Adapter<HoatDongAdapter.ViewHolder> {
    private List<UserQuizResult> resultList;
    private Context context;

    public HoatDongAdapter(Context context, List<UserQuizResult> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoat_dong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserQuizResult result = resultList.get(position);

        holder.txtMaBai.setText("Mã bài: " + result.getQuiz_id());
        holder.txtDiem.setText("Điểm: " + result.getScore());

        // Format ISO date to readable format
        String formattedDate = result.getTaken_at();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(result.getTaken_at());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtThoiGian.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaBai, txtDiem, txtThoiGian;
        Button btnXemChiTiet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtMaBai = itemView.findViewById(R.id.txtMaBai);
            txtDiem = itemView.findViewById(R.id.txtDiem);
            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);
        }
    }
}


