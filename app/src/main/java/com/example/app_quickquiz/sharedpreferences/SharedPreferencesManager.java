package com.example.app_quickquiz.sharedpreferences;
import android.content.Context;


import android.content.SharedPreferences;

// mục đích lưu trạng thái đăng nhập của người dùng
public class SharedPreferencesManager {
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_PASSWORD = "user_password";
    private static final String KEY_REMEMBER_ME = "remember_me";



    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu thông tin đăng nhập
    public void saveLoginCredentials(String email, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    public boolean isRememberMeChecked() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    // Xóa thông tin đăng nhập
    public void clearLoginCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_REMEMBER_ME);
        editor.apply();
    }
}
