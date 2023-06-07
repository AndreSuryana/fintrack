package com.andresuryana.fintrack.data.prefs;

public interface SessionHelper {

    boolean isLoggedIn();

    String getCurrentUserName();

    void setCurrentUserName(String userName);

    String getCurrentUserEmail();

    void setCurrentUserEmail(String email);

    String getCurrentUserToken();

    void setCurrentUserToken(String token);

    String getCurrentUserId();

    void setCurrentUserId(String userId);
}
