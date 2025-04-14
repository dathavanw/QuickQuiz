package com.example.app_quickquiz.model;

import java.util.List;

public class QuestionWithAnswers {
    private Question question;
    private List<Answer> answers;
    private Answer selectedAnswer; // Đáp án mà người dùng chọn

    public QuestionWithAnswers(Question question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
        this.selectedAnswer = null; // Mặc định chưa chọn đáp án
    }

    // Getter và Setter
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answer getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    // Phương thức trả về ID của đáp án được chọn
    public String getSelectedAnswerId() {
        return selectedAnswer != null ? selectedAnswer.getId() : null; // Trả về null nếu chưa chọn
    }
}