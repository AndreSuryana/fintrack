package com.andresuryana.fintrack.ui.profile;

import android.content.Context;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.data.repository.Callback;
import com.andresuryana.fintrack.data.repository.UserRepository;
import com.andresuryana.fintrack.data.repository.UserRepositoryImpl;

public class ProfilePresenter {

    private final Context context;
    private final ProfileView view;
    private final UserRepository repository;


    public ProfilePresenter(Context context, ProfileView view) {
        this.context = context;
        this.view = view;
        this.repository = new UserRepositoryImpl(context);
    }

    void loadUser() {
        try {
            repository.getUser(new Callback<User>() {
                @Override
                public void onSuccess(User result) {
                    view.onLoadUser(result);
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void updatePersonalData() {
        try {
            repository.getUser(new Callback<User>() {
                @Override
                public void onSuccess(User result) {
                    view.onLoadUser(result);
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void forgotPassword() {
        try {
            repository.forgotPassword(new Callback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    view.showMessage(context.getString(R.string.success_request_forgot_password));
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(context.getString(R.string.error_request_forgot_password));
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void logout() {
        try {
            repository.logout(new Callback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    view.showMessage(context.getString(R.string.success_logout));
                    view.onSuccessLogout();
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }
}
