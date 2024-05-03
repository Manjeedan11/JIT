package com.affc.ait.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInstance {
    private static final String PREF_NAME = "UserInstancePrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_ADMIN = "isAdmin";

    private static UserInstance instance;
    private int userId;
    private boolean isAdmin;
    private SharedPreferences sharedPreferences;

    private UserInstance(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USER_ID, -1);
        isAdmin = sharedPreferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public static synchronized UserInstance getInstance(Context context) {
        if (instance == null) {
            instance = new UserInstance(context);
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        saveToSharedPreferences(KEY_USER_ID, userId);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        saveToSharedPreferences(KEY_IS_ADMIN, isAdmin);
    }

    public void logout() {
        // Clear user data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Reset instance
        instance = null;
    }

    private void saveToSharedPreferences(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    // Optional: Prevent cloning of the singleton
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }

    // Optional: Prevent deserialization of the singleton
    protected Object readResolve() {
        return getInstance(null);
    }
}
