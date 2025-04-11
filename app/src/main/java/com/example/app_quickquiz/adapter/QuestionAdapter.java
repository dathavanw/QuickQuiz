package com.example.app_quickquiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.R;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questions; // Danh sách câu hỏi
    private Map<Integer, List<Answer>> answersMap; // Map chứa đáp án cho từng câu hỏi
    private Map<Integer, Integer> selectedAnswers = new HashMap<>(); // Theo dõi đáp án được chọn

    public QuestionAdapter(List<Question> questions, Map<Integer, List<Answer>> answersMap) {
        this.questions = questions;
        this.answersMap = answersMap;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionText.setText(question.getContent());

        // Xóa RadioButton hiện tại trong RadioGroup
        holder.answerGroup.removeAllViews();

        // Thêm các đáp án từ answersMap vào RadioGroup
        List<Answer> answers = answersMap.get(question.getId());
        if (answers != null) {
            for (Answer answer : answers) {
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(answer.getContent());
                radioButton.setId(answer.getId()); // Đặt id để theo dõi đáp án được chọn
                holder.answerGroup.addView(radioButton);
            }
        }

        // Thiết lập trạng thái đã chọn (nếu có)
        if (selectedAnswers.containsKey(question.getId())) {
            holder.answerGroup.check(selectedAnswers.get(question.getId()));
        }

        // Lắng nghe sự kiện chọn đáp án
        holder.answerGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedAnswers.put(question.getId(), checkedId); // Lưu đáp án được chọn
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioGroup answerGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            answerGroup = itemView.findViewById(R.id.answerGroup);
        }
    }
}