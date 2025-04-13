package com.example.app_quickquiz.model;

public class UserAnswer {
    private String id;
    private String quiz_result_id;
    private String question_id;
    private String answer_id;  // 1: Option 1, 2: Option 2, etc.

    public UserAnswer() {}

    public String getId() { return id; }
    public String getQuiz_result_id() { return quiz_result_id; }
    public String getQuestion_id() { return question_id; }
    public String getAnswer_id() { return answer_id; }

    public void setId(String id) { this.id = id; }
    public void setQuiz_result_id(String quiz_result_id) { this.quiz_result_id = quiz_result_id; }
    public void setQuestion_id(String question_id) { this.question_id = question_id; }
    public void setAnswer_id(String answer_id) { this.answer_id = answer_id; }
}
