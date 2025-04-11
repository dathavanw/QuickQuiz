package com.example.app_quickquiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_quickquiz.Quiz;
import com.example.app_quickquiz.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<Quiz> quizList;

    public QuizAdapter(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public void updateList(List<Quiz> newList) {
        quizList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.txtTitle.setText(quiz.getTitle());
        holder.txtDescription.setText(quiz.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(quiz.getImageUrl())
                .placeholder(R.drawable.ic_quiz_search)
                .into(holder.imgQuiz);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDescription;
        ImageView imgQuiz;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtQuizTitle);
            txtDescription = itemView.findViewById(R.id.txtQuizDescription);
            imgQuiz = itemView.findViewById(R.id.imgQuiz);
        }
    }
}

