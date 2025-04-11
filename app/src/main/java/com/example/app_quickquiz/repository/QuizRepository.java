package com.example.app_quickquiz.repository;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Quiz;

public class QuizRepository {
    private final Database db;

    public QuizRepository() {
        db = new Database();
    }

    public void getQuiz(String quizCode, Database.Callback<Quiz> callback) {
        db.getQuiz(quizCode, callback);
    }
}
