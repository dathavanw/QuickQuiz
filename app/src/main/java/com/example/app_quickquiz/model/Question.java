package com.example.app_quickquiz.model;

public class Question {
    private int id;             // ID của câu hỏi
    private int quizId;         // ID của bài quiz mà câu hỏi thuộc về
    private String content;     // Nội dung câu hỏi
    private String questionType;


    public Question() {}

    // Constructor đầy đủ
    public Question(int id, int quizId, String content, String questionType) {
        this.id = id;
        this.quizId = quizId;
        this.content = content;
        this.questionType = questionType;
    }

    // Getter và Setter cho 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho 'quizId'
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    // Getter và Setter cho 'content'
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter và Setter cho 'questionType'
    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
