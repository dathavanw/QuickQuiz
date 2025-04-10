package com.example.app_quickquiz.database;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.app_quickquiz.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    private static final FirebaseAuth mAuth  = FirebaseAuth.getInstance();                                                                    ;
    public DatabaseReference getDatabaseReference(){
        return mDatabase;
    }
    public FirebaseAuth getAuth(){
        return mAuth;
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


}
