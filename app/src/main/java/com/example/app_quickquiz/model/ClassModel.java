package com.example.app_quickquiz.model;

public class ClassModel {
    private String id;
    private String name;
    private String teacherName;
    private String imageUrl;

    public ClassModel() {}

    public ClassModel(String id, String name, String teacherName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.teacherName = teacherName;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

