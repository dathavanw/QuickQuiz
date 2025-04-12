package com.example.app_quickquiz.model;

public class QuizResult {
    private int id;
    private int quiz_id;
    private int user_id;
    private int score;
    private String taken_at;

    public QuizResult() {}

    public int getId() { return id; }
    public int getQuiz_id() { return quiz_id; }
    public int getUser_id() { return user_id; }
    public int getScore() { return score; }
    public String getTaken_at() { return taken_at; }
}
