package com.example.app_quickquiz.repository;

import android.util.Log;

import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.User;
import com.google.firebase.database.DatabaseReference;

public class UserRepository {

    private final Database db;
    public UserRepository() {
        this.db = new Database();
    }
    public void insertUser(String name,String email ,String password, String role){
            User user = new User(null,name,email,password,role);
            db.insertUser(email,password,user);
    }



}

