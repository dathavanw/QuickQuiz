package com.example.app_quickquiz.model;

public class User_Answers {
    private String id;
    private String user_id,question_id,answer_id ,quiz_result_id;

    public User_Answers(String id) {
        this.id = id;
    }

    public User_Answers() {
    }

    public User_Answers(String id, String user_id, String question_id, String answer_id, String quiz_result_id) {
        this.id = id;
        this.user_id = user_id;
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.quiz_result_id = quiz_result_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getQuiz_result_id() {
        return quiz_result_id;
    }

    public void setQuiz_result_id(String quiz_result_id) {
        this.quiz_result_id = quiz_result_id;
    }
}
