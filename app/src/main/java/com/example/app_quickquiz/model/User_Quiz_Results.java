package com.example.app_quickquiz.model;

public class User_Quiz_Results {
    private int id ;
    private String user_id, taken_at,quiz_id ;
    private int score ;

    public User_Quiz_Results(String user_id, String quiz_id, int score,String taken_at) {
        //     this.id = id;
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.score = score;
        this.taken_at = taken_at;

    }

    public User_Quiz_Results() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getTaken_at() {
        return taken_at;
    }

    public void setTaken_at(String taken_at) {
        this.taken_at = taken_at;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}