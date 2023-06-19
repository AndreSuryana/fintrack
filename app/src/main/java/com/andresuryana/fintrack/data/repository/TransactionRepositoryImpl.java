package com.andresuryana.fintrack.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.prefs.SessionHelperImpl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final DatabaseReference transactionRef;
    private final DatabaseReference userRef;

    private static final int LAST_TRANSACTIONS_DEFAULT_SIZE = 10;

    public TransactionRepositoryImpl(Context context) {
        SessionHelper session = new SessionHelperImpl(context);
        String userId = session.getCurrentUserId();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.userRef = db.getReference("users").child(userId);
        this.transactionRef = db.getReference("users").child(userId).child("transactions");
    }

    @Override
    public void addTransaction(Transaction transaction, Callback<Transaction> callback) {
        try {
            // Push new transaction into the references
            transactionRef.push().getRef().setValue(transaction);

            // Update income/outcome
            updateIncomeAndOutcome(transaction.getType(), transaction.getAmount());

            callback.onSuccess(transaction);
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void removeTransaction(Transaction transaction, Callback<Transaction> callback) {
        try {
            // Get and delete transaction references
            DatabaseReference deleteTransactionRef = transactionRef.child(transaction.getUid());
            deleteTransactionRef.removeValue((error, ref) -> {
                if (error == null) {
                    // Update income/outcome
                    updateIncomeAndOutcome(transaction.getType(), transaction.getAmount());

                    // Transaction removed successfully
                    callback.onSuccess(transaction);
                } else {
                    // Error occurred while removing the transaction
                    callback.onFailure(error.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void updateTransaction(String transactionUid, Transaction transaction, Callback<Transaction> callback) {
        try {
            // Get the specific transaction reference
            DatabaseReference updateTransactionRef = transactionRef.child(transactionUid);

            // Update the transaction object in the database
            updateTransactionRef.setValue(transaction, (error, ref) -> {
                if (error == null) {
                    // Update income/outcome
                    updateIncomeAndOutcome(transaction.getType(), transaction.getAmount());

                    // Transaction updated successfully
                    callback.onSuccess(transaction);
                } else {
                    // Error occurred while updating the transaction
                    callback.onFailure(error.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void getTransactions(@Nullable Category category, Callback<List<Transaction>> callback) {
        try {
            // Retrieve the transactions data from the database
            transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Transaction> transactions = new ArrayList<>();
                    for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                        Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                        if (transaction != null) {
                            transaction.setUid(transactionSnapshot.getKey());
                            transactions.add(transaction);
                        }
                    }
                    // Categories retrieved successfully
                    callback.onSuccess(transactions);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error occurred while retrieving the categories
                    callback.onFailure(databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void getLastTransactions(Callback<List<Transaction>> callback) {
        try {
            // Create query
            Query query = transactionRef.orderByChild("timestamp").limitToLast(LAST_TRANSACTIONS_DEFAULT_SIZE);

            // Retrieve the transactions data from the database
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<Transaction> transactions = new ArrayList<>();
                        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                            Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                            if (transaction != null) {
                                transaction.setUid(transactionSnapshot.getKey());
                                transactions.add(0, transaction);
                            }
                        }
                        callback.onSuccess(transactions);
                    } else {
                        callback.onFailure("No last transactions found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });

        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    /**
     * Function to update the amount of "income" or "outcome"
     * from the current user
     */
    private void updateIncomeAndOutcome(Transaction.Type type, long amount) {
        try {
            if (type == Transaction.Type.INCOME) {
                // Get income ref
                DatabaseReference incomeRef = userRef.child("income").getRef();

                // Proceed update income
                incomeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Update income
                            Long currentIncome = snapshot.getValue(Long.class);
                            incomeRef.setValue(currentIncome != null ? currentIncome + amount : amount);
                        } else {
                            // There is no income field
                            incomeRef.setValue(amount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.toException().printStackTrace();
                    }
                });
            } else {
                // Get outcome ref
                DatabaseReference outcomeRef = userRef.child("outcome").getRef();

                // Proceed update outcome
                outcomeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Update outcome
                            Long currentOutcome = snapshot.getValue(Long.class);
                            outcomeRef.setValue(currentOutcome != null ? currentOutcome + amount : amount);
                        } else {
                            // There is no outcome field
                            outcomeRef.setValue(amount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.toException().printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
