package com.example.app_quickquiz.model;

public class Feedback {

    private String id;
    private String email;
    private String feedback;

    public Feedback(String id, String email, String feedback) {
        this.id = id;
        this.email = email;
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFeedback() {
        return feedback;
    }
}