package com.example.app_quickquiz.model;

public class Notification {
    private String id;
    private String message;
    private String dateTime;
    private String type; // Ví dụ: "update", "system", "promotion"

    public Notification() {}

    public Notification(String id, String message, String dateTime, String type) {
        this.id = id;
        this.message = message;
        this.dateTime = dateTime;
        this.type = type;
    }

    // Getters và setters
    public String getId() { return id; }
    public String getMessage() { return message; }
    public String getDateTime() { return dateTime; }
    public String getType() { return type; }

    public void setId(String id) { this.id = id; }
    public void setMessage(String message) { this.message = message; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setType(String type) { this.type = type; }
}
