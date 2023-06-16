package com.andresuryana.fintrack.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHelperImpl implements SessionHelper {

    private static final String PREFS_NAME = "fin_track_prefs";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";

    private final SharedPreferences prefs;

    public SessionHelperImpl(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLoggedIn() {
        return getCurrentUserToken() != null;
    }

    @Override
    public String getCurrentUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    @Override
    public void setCurrentUserId(String userId) {
        prefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    @Override
    public String getCurrentUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        prefs.edit().putString(KEY_USER_NAME, userName).apply();
    }

    @Override
    public String getCurrentUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    @Override
    public void setCurrentUserEmail(String email) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    @Override
    public String getCurrentUserToken() {
        return prefs.getString(KEY_USER_TOKEN, null);
    }

    @Override
    public void setCurrentUserToken(String token) {
        prefs.edit().putString(KEY_USER_TOKEN, token).apply();
    }

    @Override
    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
