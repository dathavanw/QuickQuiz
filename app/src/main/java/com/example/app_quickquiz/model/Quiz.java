package com.example.app_quickquiz.model;

public class Quiz {
    private String id;
    private String title;
    private String description;
    private int time_limit;
    private int category_id;
    private String created_at;
    private int created_by;

    public Quiz() {}

    // Getters và setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getTime_limit() { return time_limit; }
    public int getCategory_id() { return category_id; }
    public String getCreated_at() { return created_at; }
    public int getCreated_by() { return created_by; }
}

