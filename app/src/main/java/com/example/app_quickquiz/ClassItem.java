package com.example.quickquizapp;

public class ClassItem {
    private String className;
    private String teacherName;
    private int backgroundColor;

    public ClassItem(String className, String teacherName, int backgroundColor) {
        this.className = className;
        this.teacherName = teacherName;
        this.backgroundColor = backgroundColor;
    }

    public String getClassName() {
        return className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
