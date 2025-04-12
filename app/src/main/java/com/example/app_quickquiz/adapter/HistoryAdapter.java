package com.example.app_quickquiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_quickquiz.R;
import com.example.app_quickquiz.activity.QuizActivity;
import com.example.app_quickquiz.model.QuizResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<QuizResult> resultList;

    public HistoryAdapter(Context context, List<QuizResult> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizResult result = resultList.get(position);
        holder.txtMaBai.setText("Mã bài: " + result.getQuiz_id());
        holder.txtDiem.setText("Điểm: " + result.getScore());

        try {
            String time = result.getTaken_at().replace("T", " ").replace("Z", "");
            holder.txtThoiGian.setText(time);
        } catch (Exception e) {
            holder.txtThoiGian.setText("N/A");
        }

        holder.btnXemChiTiet.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("quiz_result_id", result.getId());
            intent.putExtra("quiz_id", result.getQuiz_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaBai, txtDiem, txtThoiGian;
        Button btnXemChiTiet;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaBai = itemView.findViewById(R.id.txtMaBai);
            txtDiem = itemView.findViewById(R.id.txtDiem);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);
        }
    }
}
