package com.example.app_quickquiz.database;
import android.util.Log;

import com.example.app_quickquiz.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        mAuth.sendPasswordResetEmail(email);
    }



}
