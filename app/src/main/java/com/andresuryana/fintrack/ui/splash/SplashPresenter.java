package com.andresuryana.fintrack.ui.splash;

import android.util.Log;

import com.andresuryana.fintrack.data.prefs.SessionHelper;

public class SplashPresenter {

    private final SessionHelper session;
    private final SplashView view;

    public SplashPresenter(SessionHelper session, SplashView view) {
        this.session = session;
        this.view = view;
    }

    public void checkUserSession() {
        // If user is logged in, navigate to MainFragment
        // else, navigate to LoginFragment
        Log.d("SplashPresenter", String.format("checkUserSession: %s", session));
        if (session.isLoggedIn()) {
            view.authenticated();
        } else {
            view.unauthenticated();
        }
    }
}
