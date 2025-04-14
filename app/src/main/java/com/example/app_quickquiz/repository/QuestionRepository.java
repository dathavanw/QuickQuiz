package com.example.app_quickquiz.repository;

import com.example.app_quickquiz.database.Database;

public class QuestionRepository {
    private final Database db;

    public QuestionRepository() {
        db = new Database();
    }

//    public void getQuestionByQuizID(String id, Database.QuestionsCallback callback) {
//        db.getQuestionByQuizID(id, callback);
//    }
}
