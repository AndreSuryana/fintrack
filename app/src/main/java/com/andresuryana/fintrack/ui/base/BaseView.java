package com.andresuryana.fintrack.ui.base;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showMessage(String message);

    void showErrorMessage(String message);
}
