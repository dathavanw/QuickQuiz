package com.example.app_quickquiz.model;

public class Answer {
    private int id;
    private int question_id;
    private String content;
    private boolean is_correct;

    public Answer() {}

    public int getId() { return id; }
    public int getQuestion_id() { return question_id; }
    public String getContent() { return content; }
    public boolean isIs_correct() { return is_correct; }
}
