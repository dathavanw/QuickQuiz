package com.example.app_quickquiz.model;

public class Answer {
    private String id;
    private String question_id;
    private String answer;
    private boolean is_correct;

    public Answer( String id, String content, boolean isCorrect,String  questionId) {
        this.id = id;
        this.answer = content;
        this.is_correct = isCorrect;
        this.question_id = questionId;
    }

    public Answer() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }



    public boolean isCorrect() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public int getContent() {
        return 0;
    }

    public String getId() {
        return id;
    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
}
