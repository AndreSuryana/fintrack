package com.andresuryana.fintrack.ui.auth.login;

import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.ui.base.BaseView;

public interface LoginView extends BaseView {

    void onLoginSuccess(User user);

    void onLoginFailure(String message);
}
