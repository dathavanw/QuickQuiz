package com.example.app_quickquiz.model;

public class UserAnswer {
    private int id;
    private int quiz_result_id;
    private int question_id;
    private int answer_id;  // 1: Option 1, 2: Option 2, etc.

    public UserAnswer() {}

    public int getId() { return id; }
    public int getQuiz_result_id() { return quiz_result_id; }
    public int getQuestion_id() { return question_id; }
    public int getAnswer_id() { return answer_id; }

    public void setId(int id) { this.id = id; }
    public void setQuiz_result_id(int quiz_result_id) { this.quiz_result_id = quiz_result_id; }
    public void setQuestion_id(int question_id) { this.question_id = question_id; }
    public void setAnswer_id(int answer_id) { this.answer_id = answer_id; }
}
