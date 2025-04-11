package com.example.quickquizapp;

public class UserQuizResult {
    public int quiz_id;
    public int score;
    public String taken_at;
    public int user_id;

    public UserQuizResult() {
        // Required for Firebase
    }

    public UserQuizResult(int quiz_id, int score, String taken_at, int user_id) {
        this.quiz_id = quiz_id;
        this.score = score;
        this.taken_at = taken_at;
        this.user_id = user_id;
    }

    public int getQuiz_id() { return quiz_id; }
    public int getScore() { return score; }
    public String getTaken_at() { return taken_at; }
    public int getUser_id() { return user_id; }
}

