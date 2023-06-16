package com.andresuryana.fintrack.ui.dashboard;

import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.ui.base.BaseView;

import java.util.List;

public interface DashboardView extends BaseView {

    void onLoadDashboardInfo(long income, long outcome);

    void onLoadLastTransaction(List<Transaction> transactions);
}
