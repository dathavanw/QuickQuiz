package com.example.quickquizapp;

public class Question {
    private int imageResId;
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;

    public Question(int imageResId, String questionText, String[] options, int correctAnswerIndex) {
        this.imageResId = imageResId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
