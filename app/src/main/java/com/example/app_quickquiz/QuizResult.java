package com.example.app_quickquiz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuizResult {
    private int quiz_id;
    private int score;
    private String taken_at;
    private int user_id;

    public QuizResult() {
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public int getScore() {
        return score;
    }

    public String getTaken_at() {
        return taken_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public Date getTakenDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(taken_at);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
}
