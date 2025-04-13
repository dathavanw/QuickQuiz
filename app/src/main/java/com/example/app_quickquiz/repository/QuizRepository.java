package com.example.app_quickquiz.repository;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;
import com.example.app_quickquiz.model.QuestionWithAnswers;
import com.example.app_quickquiz.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuizRepository {
    private final Database db;

    public QuizRepository() {
        db = new Database();
    }

    public void checkQuizExistsById(String id, Database.QuizExistCallback callback) {
        db.checkQuizExistsById(id, callback);
    }







    // Lấy danh sách câu hỏi kèm theo đáp án
    public void getQuestionsWithAnswersByQuizId(String quizId, Database.OnGetDataListener<List<QuestionWithAnswers>> listener) {
            db.getQuestionByQuizID(quizId, new Database.OnGetDataListener<List<Question>>() {
            @Override
            public void onSuccess(List<Question> questions) {
                List<QuestionWithAnswers> result = new ArrayList<>();
                AtomicInteger counter = new AtomicInteger(0); // Đếm số câu hỏi đã xử lý
                if (questions.isEmpty()) {
                    listener.onSuccess(result); // Không có câu hỏi
                    return;
                }
                for (Question question : questions) {
                    db.getAnswersByQuestionId(question.getId(), new Database.OnGetDataListener<List<Answer>>() {
                        @Override
                        public void onSuccess(List<Answer> answers) {
                            result.add(new QuestionWithAnswers(question, answers));

                            // Khi đã xử lý hết tất cả câu hỏi thì trả về kết quả
                            if (counter.incrementAndGet() == questions.size()) {
                                listener.onSuccess(result);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            listener.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }



}