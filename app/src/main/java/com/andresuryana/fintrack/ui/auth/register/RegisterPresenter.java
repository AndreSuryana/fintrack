package com.andresuryana.fintrack.ui.auth.register;

import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.repository.UserRepository;
import com.andresuryana.fintrack.data.repository.UserRepositoryImpl;

public class RegisterPresenter {

    private final UserRepository repository;
    private final SessionHelper session;
    private final RegisterView view;

    public RegisterPresenter(SessionHelper session, RegisterView view) {
        this.repository = new UserRepositoryImpl();
        this.session = session;
        this.view = view;
    }

    public void register(String name, String email, String password) {
        User user = User.createForRegister(name, email, password);
        repository.register(user, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                // Update session
                session.setCurrentUserId(user.getUserId());
                session.setCurrentUserName(user.getName());
                session.setCurrentUserEmail(user.getEmail());
                session.setCurrentUserToken(user.getToken());

                view.onRegisterSuccess(user);
            }

            @Override
            public void onFailure(String message) {
                view.onRegisterFailure(message);
            }
        });
    }
}
