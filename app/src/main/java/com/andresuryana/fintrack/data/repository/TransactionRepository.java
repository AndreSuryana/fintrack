package com.andresuryana.fintrack.data.repository;

import androidx.annotation.Nullable;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void addTransaction(Transaction transaction, Callback<Void> callback);

    void removeTransaction(Transaction transaction, Callback<Void> callback);

    void updateTransaction(String transactionUid, Transaction transaction, Callback<Void> callback);

    void getTransactions(@Nullable Category category, Callback<List<Transaction>> callback);
}
