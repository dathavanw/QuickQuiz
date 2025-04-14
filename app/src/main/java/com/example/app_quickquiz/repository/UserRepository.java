package com.example.app_quickquiz.repository;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.User;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    private final Database db;

    public UserRepository() {
        this.db = new Database();
    }

    // Đăng ký tài khoản
    public void insertUser(String name, String email, String password, String role) {
        User user = new User(null, name, email, role);
        db.insertUser(email, password, user);
    }

    // Quên mật khẩu
    public void sendPasswordReset(String email) {
        db.sendPasswordReset(email);
    }

    // Lấy thông tin người dùng từ uid
    public void getUserInfor(String uid, Database.DatabaseCallback callback) {
        db.getUserInfor(uid, new Database.DatabaseCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    // Lấy role từ uid
    public void getUserRole(String uid, Database.RoleCallback callback) {
        db.getUserRole(uid, callback);
    }

    // Đăng nhập (giản lược: chỉ 3 đối số)
    public void loginWithEmailAndPassword(String email, String password, Database.LoginCallback callback) {
        db.loginWithEmailAndPassword(email, password, new Database.LoginCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                callback.onSuccess(user); // thành công thì gọi callback
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage); // thất bại thì báo lỗi
            }
        });
    }

    public void insertUserWithGoogle(FirebaseUser firebaseUser, String role) {
        // Chức năng này bạn có thể bổ sung sau nếu dùng Google Sign-In
    }
}
