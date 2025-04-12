package com.example.app_quickquiz.repository;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Quiz;

public class QuizRepository {
    private final Database db;

    public QuizRepository() {
        db = new Database();
    }

    public void checkQuizExistsById(int id, Database.QuizExistCallback callback) {
        db.checkQuizExistsById(id, callback);
    }
}
