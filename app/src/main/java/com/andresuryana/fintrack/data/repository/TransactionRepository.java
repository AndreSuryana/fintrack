package com.andresuryana.fintrack.data.repository;

import androidx.annotation.Nullable;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void addTransaction(Transaction transaction, Callback<Transaction> callback);

    void removeTransaction(Transaction transaction, Callback<Transaction> callback);

    void updateTransaction(Transaction oldTransaction, Transaction transaction, Callback<Transaction> callback);

    void getTransactions(@Nullable Category category, Callback<List<Transaction>> callback);

    void getLastTransactions(Callback<List<Transaction>> callback);
}
