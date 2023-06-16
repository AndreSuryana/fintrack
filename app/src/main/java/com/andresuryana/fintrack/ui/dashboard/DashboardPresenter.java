package com.andresuryana.fintrack.ui.dashboard;

import android.content.Context;

import com.andresuryana.fintrack.data.model.DashboardInfo;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.data.repository.Callback;
import com.andresuryana.fintrack.data.repository.TransactionRepository;
import com.andresuryana.fintrack.data.repository.TransactionRepositoryImpl;

import java.util.List;

public class DashboardPresenter {

    private final DashboardView view;
    private final TransactionRepository repository;


    public DashboardPresenter(Context context, DashboardView view) {
        this.view = view;
        this.repository = new TransactionRepositoryImpl(context);
    }

    void loadDashboardInfo() {
        // TODO: This is temporary, please update Firebase Database for this action
        try {
            repository.getTransactions(null, new Callback<List<Transaction>>() {
                @Override
                public void onSuccess(List<Transaction> result) {
                    // Define variable
                    long income = 0;
                    long outcome = 0;

                    // Loop through transactions
                    for (Transaction transaction : result) {
                        if (transaction.getType() == Transaction.Type.INCOME) {
                            income += transaction.getAmount();
                        } else {
                            outcome += transaction.getAmount();
                        }
                    }

                    view.onLoadDashboardInfo(new DashboardInfo(income, outcome));
                }

                @Override
                public void onFailure(String message) {

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
