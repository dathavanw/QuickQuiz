package com.example.app_quickquiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.QuestionWithAnswers;

import java.util.HashMap;
import java.util.List;

public class QuestionWithAnswersAdapter extends RecyclerView.Adapter<QuestionWithAnswersAdapter.QuestionViewHolder> {

    private final Context context;
    private final List<QuestionWithAnswers> questionList;
    // Lưu đáp án được chọn cho mỗi câu hỏi
    private final HashMap<Integer, Integer> selectedAnswers = new HashMap<>();
    public QuestionWithAnswersAdapter(Context context, List<QuestionWithAnswers> questionList) {
        this.context = context;
        this.questionList = questionList;
    }
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question_with_answers, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionWithAnswers qwa = questionList.get(position);
        holder.tvQuestion.setText(qwa.getQuestion().getContent());

        holder.rgAnswers.removeAllViews(); // Xoá các RadioButton cũ

        List<Answer> answers = qwa.getAnswers();

        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);

            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(answer.getAnswer());
            radioButton.setTextColor(Color.BLACK);
            radioButton.setId(i);  // ID cho từng RadioButton trong nhóm

            holder.rgAnswers.addView(radioButton);
        }

        // Xử lý sự kiện chọn đáp án
        holder.rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            selectedAnswers.put(position, checkedId);
        });

        // Phục hồi trạng thái đã chọn (nếu có)
        if (selectedAnswers.containsKey(position)) {
            holder.rgAnswers.check(selectedAnswers.get(position));
        } else {
            holder.rgAnswers.clearCheck();
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        RadioGroup rgAnswers;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestionContent);
            rgAnswers = itemView.findViewById(R.id.rgAnswers);
        }
    }
    // Hàm trả về đáp án đã chọn của từng câu hỏi
//    public HashMap<Integer, Integer> getSelectedAnswers() {
//        return selectedAnswers;
//    }

}
