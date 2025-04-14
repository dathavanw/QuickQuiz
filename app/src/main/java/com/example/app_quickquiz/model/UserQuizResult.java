package com.example.app_quickquiz.model;

public class UserQuizResult {
    private String id;
    private String quiz_id;
    private int score;
    private String taken_at;
    private String user_id;

    public UserQuizResult() {
    }

    public String getId() {
        return id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public int getScore() {
        return score;
    }

    public String getTaken_at() {
        return taken_at;
    }

    public String getUser_id() {
        return user_id;
    }
}
