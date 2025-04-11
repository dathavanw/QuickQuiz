package com.example.app_quickquiz;

public class HoatDong {
    private int quiz_id;
    private int score;
    private String taken_at;
    private int user_id;

    public HoatDong() {}

    public HoatDong(int quiz_id, int score, String taken_at, int user_id) {
        this.quiz_id = quiz_id;
        this.score = score;
        this.taken_at = taken_at;
        this.user_id = user_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTaken_at() {
        return taken_at;
    }

    public void setTaken_at(String taken_at) {
        this.taken_at = taken_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
