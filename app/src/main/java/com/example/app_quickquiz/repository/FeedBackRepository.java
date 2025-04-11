package com.example.app_quickquiz.repository;

import com.example.app_quickquiz.database.Database;

public class FeedBackRepository {
    private final Database db;

    public FeedBackRepository() {
        db = new Database();
    }

    public void sendFeedback(String email, String feedback, FeedbackCallback callback) {
        db.saveFeedbackToFirebase(email, feedback, callback);
    }

    public interface FeedbackCallback {
        void onComplete(boolean isSuccess);
    }

}
