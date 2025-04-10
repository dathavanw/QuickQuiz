package com.example.app_quickquiz.repository;

import android.util.Log;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class UserRepository {

    private final Database db;
    public UserRepository() {
        this.db = new Database();
    }
    public void insertUser(String name,String email ,String password, String role){
            User user = new User(null,name,email,role);
            db.insertUser(email,password,user);
    }


    // Quên mật khẩu
    public void sendPasswordReset(String email){
        db.sendPasswordReset(email);
    }

    // lấy role người dùng
    public void getUserRole(String uid ,Database.RoleCallback callback){
        db.getUserRole(uid,callback);
    }

    // đăng nhập
    public void loginWithEmailAndPassword(String email, String password, Database.LoginCallback loginCallback, Database.RoleCallback roleCallback) {
        db.loginWithEmailAndPassword(email, password, new Database.LoginCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loginCallback.onSuccess(user); // thông báo đăng nhập thành công
                db.getUserRole(user.getUid(), roleCallback);
            }

            @Override
            public void onFailure(String errorMessage) {
                loginCallback.onFailure(errorMessage);
            }
        });
    }




}

