package com.example.quickquizapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private List<Question> questionList;
    private Context context;

    public QuizAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.imgQuestion.setImageResource(question.getImageResId());
        holder.tvQuestion.setText(question.getQuestionText());

        Button[] buttons = {holder.btnOption1, holder.btnOption2, holder.btnOption3, holder.btnOption4};

        for (int i = 0; i < 4; i++) {
            buttons[i].setText(question.getOptions()[i]);
            buttons[i].setBackgroundColor(Color.parseColor("#D1D5DB")); // Reset màu nền
            buttons[i].setEnabled(true);
            final int selectedIndex = i;

            buttons[i].setOnClickListener(v -> {
                if (selectedIndex == question.getCorrectAnswerIndex()) {
                    buttons[selectedIndex].setBackgroundColor(Color.parseColor("#22C55E")); // Màu xanh nếu đúng
                } else {
                    buttons[selectedIndex].setBackgroundColor(Color.RED); // Màu đỏ nếu sai
                }

                // Vô hiệu hóa các nút sau khi chọn
                for (Button btn : buttons) {
                    btn.setEnabled(false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgQuestion;
        TextView tvQuestion;
        Button btnOption1, btnOption2, btnOption3, btnOption4;
        ImageButton btnPrevious, btnNext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            btnOption1 = itemView.findViewById(R.id.btnOption1);
            btnOption2 = itemView.findViewById(R.id.btnOption2);
            btnOption3 = itemView.findViewById(R.id.btnOption3);
            btnOption4 = itemView.findViewById(R.id.btnOption4);
            btnPrevious = itemView.findViewById(R.id.btnPrevious);
            btnNext = itemView.findViewById(R.id.btnNext);
        }
    }
}
