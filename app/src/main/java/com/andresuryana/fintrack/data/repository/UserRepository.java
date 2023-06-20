package com.andresuryana.fintrack.data.repository;

import com.andresuryana.fintrack.data.model.DashboardInfo;
import com.andresuryana.fintrack.data.model.User;

public interface UserRepository {

    void login(User user, final AuthCallback callback);

    void register(User user, final AuthCallback callback);

    void logout(Callback<Boolean> callback);

    void getDashboardInfo(Callback<DashboardInfo> callback);

    void getUser(Callback<User> callback);

    void forgotPassword(Callback<Boolean> callback);

    interface AuthCallback {

        void onSuccess(User user);

        void onFailure(String message);
    }
}
