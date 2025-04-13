package com.example.app_quickquiz.database;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.Feedback;
import com.example.app_quickquiz.model.Question;
import com.example.app_quickquiz.model.Quiz;
import com.example.app_quickquiz.model.User;
import com.example.app_quickquiz.model.User_Answers;
import com.example.app_quickquiz.model.User_Quiz_Results;
import com.example.app_quickquiz.repository.FeedBackRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    private static final FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    private static final DatabaseReference FeedbackDatabase = FirebaseDatabase.getInstance().getReference("feedbacks");
    private static final DatabaseReference quizRef  = FirebaseDatabase.getInstance().getReference("Quizzes");
    private static final DatabaseReference questionRef  = FirebaseDatabase.getInstance().getReference("Questions");
    private static final DatabaseReference answerRef  = FirebaseDatabase.getInstance().getReference("Answers");
//    public DatabaseReference getDatabaseReference(){
//        return mDatabase;
//    }
//    public FirebaseAuth getAuth(){
//        return mAuth;
//    }

    public void saveFeedbackToFirebase(String email, String feedback, FeedBackRepository.FeedbackCallback callback) {
        String feedbackId = FeedbackDatabase.push().getKey();
        Feedback feedbackObj = new Feedback(feedbackId, email, feedback);
        FeedbackDatabase.child(feedbackId).setValue(feedbackObj)
                .addOnCompleteListener(task -> callback.onComplete(task.isSuccessful()))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    // đăng ký người dùng
    public  void  insertUser(String email,String password , User user){
       mAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       String userID = mAuth.getCurrentUser().getUid();
                       user.setId(userID);
                       mDatabase.child(userID).setValue(user)
                               .addOnCompleteListener(saveTask -> {
                                   if(saveTask.isSuccessful()){
                                       Log.d("Database","User saved successfully");
                                   }else {
                                       Log.e("Database","Failed to save user" + saveTask.getException().getMessage());
                                   }
                               });
                   }else {
                       Log.e("Auth","Sign up failed !"+ task.getException().getMessage());
                   }
               });
    }


    // Quên mật khẩu
    public void sendPasswordReset(String email){
        mAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("LoginTest", "Mật khẩu đã được cập nhật thành công");
            } else {
                Log.e("LoginTest", "Đăng nhập thất bại: " + task.getException().getMessage());
            }
        });
    }



    // lấy role  của người dùng
    public void getUserRole(String uid, RoleCallback callback) {
        DatabaseReference ref = mDatabase.child(uid).child("role");
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String role = task.getResult().getValue(String.class);
                if (role != null) {
                    callback.onRoleReceived(role);
                } else {
                    callback.onError("Không tìm thấy role");
                }
            } else {
                callback.onError("Lỗi khi lấy role");
            }
        });
    }


    // đăng nhập
    public void loginWithEmailAndPassword(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();
                        callback.onSuccess(user);
                    } else {
                        Log.e("AuthError", "Lỗi đăng nhập: ", task.getException());
                        callback.onFailure("Email hoặc mật khẩu sai");
                    }
                });

    }
    // Interface callback cho login
    public interface LoginCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String e);
    }

    // Interface callback cho role
    public interface RoleCallback {
        void onRoleReceived(String role);
        void onError(String e);
    }


    // đăng nhập bằng google
    public void insertUserWithGoogle(String uid, User user) {

    }



    // lấy thông tin người dùng
    public void getUserInfor(String uid , DatabaseCallback callback){
        DatabaseReference userRef = mDatabase.child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (callback != null) {
                    callback.onSuccess(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (callback != null) {
                    callback.onError(databaseError.toException());
                }
            }
        });

    }

    public interface DatabaseCallback {
        void onSuccess(User user);
        void onError(Exception e);
    }

    // lấy mã bài Quiz
//    public void getQuiz(String codeQuiz , Callback<Quiz> callback){
//        quizRef.child(codeQuiz);
//        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Quiz quiz = snapshot.getValue(Quiz.class);
//                    callback.onSuccess(quiz);
//                } else {
//                    callback.onSuccess(null);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                callback.onError(error.toException());
//            }
//        });
//    }

    // kiểm tra sự tồn tại của quiz
    public void checkQuizExistsById(String id , QuizExistCallback callback){
        Query query = quizRef.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onResult(snapshot.exists(), snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false, null);
            }
        });
    }


    public interface QuizExistCallback {
        void onResult(boolean exists, DataSnapshot quizSnapshot);
    }



    // lấy danh sách câu hỏi của bài Quiz
    public void getQuestionByQuizID(String quiz_id, OnGetDataListener <List<Question>> listener){
            questionRef.orderByChild("quiz_id").equalTo(quiz_id)
           .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Question> questions = new ArrayList<>();
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    Question question = questionSnapshot.getValue(Question.class);
                    if (question != null) {
                        questions.add(question);
                    }
                }
                listener.onSuccess(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.toException());
            }
        });
    }
    // Lấy danh sách đáp án theo question_id
    public void getAnswersByQuestionId(String questionId, OnGetDataListener<List<Answer>> listener) {
        answerRef.orderByChild("question_id").equalTo(questionId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Answer> answers = new ArrayList<>();
                        for (DataSnapshot answerSnapshot : snapshot.getChildren()) {
                            Log.d("FirebaseAnswer",answerSnapshot.toString());
                            Answer answer = answerSnapshot.getValue(Answer.class);
                            if (answer != null) {
                                answers.add(answer);
                            }
                        }
                        listener.onSuccess(answers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailure(error.toException());
                    }
                });
    }

    public interface OnGetDataListener<T> {
        void onSuccess(T data);
        void onFailure(Exception e);
    }






    // lưu kết quả bài làm
    public void saveUserQuizResults(User_Quiz_Results userQuizResult) {
        DatabaseReference quizResultsRef = FirebaseDatabase.getInstance().getReference("User_Quiz_Results");
        String key = quizResultsRef.push().getKey();
        quizResultsRef.child(key).setValue(userQuizResult);
    }

    // lưu chi tiết bài làm
    public void saveUserAnswers(List<User_Answers> userAnswers) {
        DatabaseReference userAnswersRef = FirebaseDatabase.getInstance().getReference("User_Answers");
        for (User_Answers answer : userAnswers) {
            String key = userAnswersRef.push().getKey();
            userAnswersRef.child(key).setValue(answer);
        }
    }














}





















