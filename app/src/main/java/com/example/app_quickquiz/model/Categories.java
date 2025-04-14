package com.example.app_quickquiz.model;

public class Categories {
    private String id;
    private String name;

    public Categories() {
    }

    // Constructor đầy đủ
    public Categories(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Optional: toString
    @Override
    public String toString() {
        return name;
    }
}
