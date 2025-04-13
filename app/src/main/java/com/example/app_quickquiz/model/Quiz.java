package com.example.app_quickquiz.model;

public class Quiz {
    private String  id,title, description , created_at;
    private int category_id,created_by;
    private int time_limit ;

    public Quiz(String id, String title, String description, int created_by, String created_at, int category_id, int time_limit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_by = created_by;
        this.created_at = created_at;
        this.category_id = category_id;
        this.time_limit = time_limit;
    }

    public Quiz() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(int time_limit) {
        this.time_limit = time_limit;
    }
}
