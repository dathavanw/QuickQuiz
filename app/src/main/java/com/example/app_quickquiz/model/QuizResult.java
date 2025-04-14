package com.example.app_quickquiz.model;

public class QuizResult {
    private int id;
    private String quiz_id;
    private String user_id;
    private int score;
    private String taken_at;

    public QuizResult() {}

    public int getId() { return id; }
    public String getQuiz_id() { return quiz_id; }
    public String getUser_id() { return user_id; }
    public int getScore() { return score; }
    public String getTaken_at() { return taken_at; }
}
