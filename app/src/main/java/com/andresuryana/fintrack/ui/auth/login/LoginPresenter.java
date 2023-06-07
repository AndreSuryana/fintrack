package com.andresuryana.fintrack.ui.auth.login;

import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.repository.UserRepository;
import com.andresuryana.fintrack.data.repository.UserRepositoryImpl;

public class LoginPresenter {

    private final UserRepository repository;
    private final SessionHelper session;
    private final LoginView view;

    public LoginPresenter(SessionHelper session, LoginView view) {
        this.repository = new UserRepositoryImpl();
        this.session = session;
        this.view = view;
    }

    public void login(String email, String password) {
        User user = User.createForLogin(email, password);
        repository.login(user, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                // Update session
                session.setCurrentUserId(user.getUserId());
                session.setCurrentUserName(user.getName());
                session.setCurrentUserEmail(user.getEmail());
                session.setCurrentUserToken(user.getToken());

                view.onLoginSuccess(user);
            }

            @Override
            public void onFailure(String message) {
                view.onLoginFailure(message);
            }
        });
    }
}
