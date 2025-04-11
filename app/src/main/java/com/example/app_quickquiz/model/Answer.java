package com.example.app_quickquiz.model;

public class Answer {
    private int id;
    private int questionId;
    private String content;
    private boolean isCorrect;

    public Answer(int id, int questionId, String content, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
