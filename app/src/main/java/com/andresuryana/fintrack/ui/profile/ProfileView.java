package com.andresuryana.fintrack.ui.profile;

import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.ui.base.BaseView;

public interface ProfileView extends BaseView {

    void onLoadUser(User user);

    void onSuccessLogout();
}
