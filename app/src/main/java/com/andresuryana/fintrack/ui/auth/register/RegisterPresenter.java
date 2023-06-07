package com.andresuryana.fintrack.ui.auth.register;

import android.content.Context;

import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.prefs.SessionHelperImpl;
import com.andresuryana.fintrack.data.repository.CategoryRepository;
import com.andresuryana.fintrack.data.repository.CategoryRepositoryImpl;
import com.andresuryana.fintrack.data.repository.UserRepository;
import com.andresuryana.fintrack.data.repository.UserRepositoryImpl;

public class RegisterPresenter {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SessionHelper session;
    private final RegisterView view;

    public RegisterPresenter(Context context, RegisterView view) {
        this.userRepository = new UserRepositoryImpl();
        this.categoryRepository = new CategoryRepositoryImpl(context);
        this.session = new SessionHelperImpl(context);
        this.view = view;
    }

    public void register(String name, String email, String password) {
        User user = User.createForRegister(name, email, password);
        userRepository.register(user, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                // Update session
                session.setCurrentUserId(user.getUserId());
                session.setCurrentUserName(user.getName());
                session.setCurrentUserEmail(user.getEmail());
                session.setCurrentUserToken(user.getToken());

                // Inject default categories
                categoryRepository.insertCategoriesForNewUser(user.getUserId());

                view.onRegisterSuccess(user);
            }

            @Override
            public void onFailure(String message) {
                view.onRegisterFailure(message);
            }
        });
    }
}
