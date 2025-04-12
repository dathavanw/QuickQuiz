package com.example.app_quickquiz.model;

public class Question {
    private String id;             // ID của câu hỏi
    private String quiz_id;         // ID của bài quiz mà câu hỏi thuộc về
    private String content;     // Nội dung câu hỏi
    private String question_type;


    public Question() {}

    // Constructor đầy đủ
        public Question(String content, String id, String questionType, String quizId) {
        this.id = id;
        this.quiz_id = quizId;
        this.content = content;
        this.question_type = questionType;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }
}
