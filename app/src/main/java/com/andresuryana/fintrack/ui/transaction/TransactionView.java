package com.andresuryana.fintrack.ui.transaction;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.ui.base.BaseView;

import java.util.List;

public interface TransactionView extends BaseView {

    void showTransactions(List<Transaction> transactions);

    void onLoadCategories(List<Category> categories);

    void showAddTransactionBottomSheet();

    void showModifyTransactionBottomSheet(Transaction transaction);
}
