package com.andresuryana.fintrack.ui.dashboard;

import android.content.Context;

import com.andresuryana.fintrack.data.model.DashboardInfo;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.data.repository.Callback;
import com.andresuryana.fintrack.data.repository.TransactionRepository;
import com.andresuryana.fintrack.data.repository.TransactionRepositoryImpl;
import com.andresuryana.fintrack.data.repository.UserRepository;
import com.andresuryana.fintrack.data.repository.UserRepositoryImpl;

import java.util.List;

public class DashboardPresenter {

    private final DashboardView view;
    private final TransactionRepository repository;
    private final UserRepository userRepository;


    public DashboardPresenter(Context context, DashboardView view) {
        this.view = view;
        this.repository = new TransactionRepositoryImpl(context);
        this.userRepository = new UserRepositoryImpl(context);
    }

    void loadDashboardInfo() {
        try {
            userRepository.getDashboardInfo(new Callback<DashboardInfo>() {
                @Override
                public void onSuccess(DashboardInfo result) {
                    view.onLoadDashboardInfo(result);
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

    void loadLastTransactions() {
        try {
            view.showLoading();
            repository.getLastTransactions(new Callback<List<Transaction>>() {
                @Override
                public void onSuccess(List<Transaction> result) {
                    view.onLoadLastTransaction(result);
                    view.hideLoading();
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                    view.hideLoading();
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
            view.hideLoading();
        }
    }
}
