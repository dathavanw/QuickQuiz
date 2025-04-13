package com.example.app_quickquiz.model;

public class Question {
    private int id;
    private int quiz_id;
    private String content;
    private String question_type;

    public Question() {}

    public int getId() { return id; }
    public int getQuiz_id() { return quiz_id; }
    public String getContent() { return content; }
    public String getQuestion_type() { return question_type; }
}
