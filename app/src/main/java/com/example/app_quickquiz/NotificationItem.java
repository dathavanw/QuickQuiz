package com.example.quickquizapp;

public class NotificationItem {
    private final int iconResId;
    private final String dateTime;
    private final String message;

    public NotificationItem(int iconResId, String dateTime, String message) {
        this.iconResId = iconResId;
        this.dateTime = dateTime;
        this.message = message;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }
}
