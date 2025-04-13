package com.example.app_quickquiz.repository;

import android.util.Log;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Question;
import com.example.app_quickquiz.model.QuestionWithAnswers;
import com.example.app_quickquiz.model.Quiz;
import com.example.app_quickquiz.model.User_Answers;
import com.example.app_quickquiz.model.User_Quiz_Results;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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


    // lưu kết quả bài làm
    public void submitQuizResults(String userId, String quizId, List<User_Answers> userAnswers) {
        calculateScore(userAnswers, new ScoreCallback() {
            @Override
            public void onScoreCalculated(int score) {
                // Bước 1: Tính điểm
                User_Quiz_Results userQuizResult = new User_Quiz_Results(userId, quizId, score, getCurrentTimestamp());

                // Bước 2: Lưu kết quả bài làm
                db.saveUserQuizResults(userQuizResult);

                // Bước 3: Lưu chi tiết từng đáp án
                db.saveUserAnswers(userAnswers);
            }
        });
    }
        public String getCurrentTimestamp() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            return sdf.format(new Date());
        }


    public interface ScoreCallback {
        void onScoreCalculated(int score);
    }


    public void calculateScore(List<User_Answers> userAnswers, ScoreCallback callback) {
        int[] score = {0}; // Dùng mảng để lưu điểm
        AtomicInteger pendingTasks = new AtomicInteger(userAnswers.size());

        for (User_Answers answer : userAnswers) {
            DatabaseReference answersRef = FirebaseDatabase.getInstance().getReference("Answers").child(answer.getAnswer_id());
            answersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean isCorrect = dataSnapshot.child("is_correct").getValue(Boolean.class);
                    if (Boolean.TRUE.equals(isCorrect)) {
                        score[0] += 10;
                    }
                    if (pendingTasks.decrementAndGet() == 0) {
                        callback.onScoreCalculated(score[0]);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FirebaseError", databaseError.getMessage());
                    if (pendingTasks.decrementAndGet() == 0) {
                        callback.onScoreCalculated(score[0]); // Trả về kết quả, kể cả khi có lỗi
                    }
                }
            });
        }
    }
}