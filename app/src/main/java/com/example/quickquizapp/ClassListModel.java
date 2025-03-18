package com.example.quickquizapp;

public class ClassListModel {
    private String title;
    private String teacher;
    private int imageResource;

    public ClassListModel(String title, String teacher, int imageResource) {
        this.title = title;
        this.teacher = teacher;
        this.imageResource = imageResource;
    }

    public String getTitle() { return title; }
    public String getTeacher() { return teacher; }
    public int getImageResource() { return imageResource; }
}

