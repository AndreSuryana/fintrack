package com.andresuryana.fintrack.ui.auth.register;

import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.ui.base.BaseView;

public interface RegisterView extends BaseView {

    void onRegisterSuccess(User user);

    void onRegisterFailure(String message);
}
