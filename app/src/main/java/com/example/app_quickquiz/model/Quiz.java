package com.example.app_quickquiz.model;

public class Quiz {
    private int id;
    private int category_id;
    private String created_at;
    private int created_by;
    private String description;
    private int time_limit;
    private String title;

    public Quiz() {}

    public int getId() { return id; }
    public int getCategory_id() { return category_id; }
    public String getCreated_at() { return created_at; }
    public int getCreated_by() { return created_by; }
    public String getDescription() { return description; }
    public int getTime_limit() { return time_limit; }
    public String getTitle() { return title; }
}

