package com.andresuryana.fintrack.ui.transaction;

import android.content.Context;

import androidx.annotation.Nullable;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.data.model.Transaction.Type;
import com.andresuryana.fintrack.data.repository.Callback;
import com.andresuryana.fintrack.data.repository.CategoryRepository;
import com.andresuryana.fintrack.data.repository.CategoryRepositoryImpl;
import com.andresuryana.fintrack.data.repository.TransactionRepository;
import com.andresuryana.fintrack.data.repository.TransactionRepositoryImpl;

import java.util.Date;
import java.util.List;

public class TransactionPresenter {

    private final Context context;
    private final TransactionView view;
    private final TransactionRepository repository;
    private final CategoryRepository categoryRepository;

    private Category selectedCategory; // TODO: Soon add chip/dropdown into the layout to implement this

    public TransactionPresenter(Context context, TransactionView view) {
        this.context = context;
        this.view = view;
        this.repository = new TransactionRepositoryImpl(context);
        this.categoryRepository = new CategoryRepositoryImpl(context);
    }

    void addTransaction(Type type, String title, long amount, Date date, @Nullable String notes, Category category) {
        try {
            if (title.isEmpty() || amount == 0 || date == null) {
                view.showErrorMessage(context.getString(R.string.error_check_input));
            } else {
                Transaction transaction = new Transaction(type, title, amount, date, notes);
                if (category != null) {
                    transaction.setCategoryIconName(category.getIconName());
                    transaction.setCategoryName(category.getName());
                }
                repository.addTransaction(transaction, new Callback<Transaction>() {
                    @Override
                    public void onSuccess(Transaction result) {
                        view.showMessage(context.getString(R.string.success_add_transaction, transaction.getTitle()));
                        loadTransactions();
                    }

                    @Override
                    public void onFailure(String message) {
                        view.showErrorMessage(message);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage(e.getMessage());
        }
    }

    void updateTransaction(Transaction oldTransaction, Type type, String title, long amount, Date date, @Nullable String notes, Category category) {
        try {
            if (title.isEmpty() || amount == 0 || date == null) {
                view.showErrorMessage(context.getString(R.string.error_check_input));
            } else {
                Transaction transaction = new Transaction(type, title, amount, date, notes);
                if (category != null) {
                    transaction.setCategoryIconName(category.getIconName());
                    transaction.setCategoryName(category.getName());
                }
                repository.updateTransaction(oldTransaction.getUid(), transaction, new Callback<Transaction>() {
                    @Override
                    public void onSuccess(Transaction result) {
                        view.showMessage(context.getString(R.string.success_add_transaction, transaction.getTitle()));
                        loadTransactions();
                    }

                    @Override
                    public void onFailure(String message) {
                        view.showErrorMessage(message);
                    }
                });
            }
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void removeTransaction(Transaction transaction) {
        try {
            repository.removeTransaction(transaction, new Callback<Transaction>() {
                @Override
                public void onSuccess(Transaction result) {
                    view.showMessage(context.getString(R.string.success_remove_transaction, result.getTitle()));
                    loadTransactions();
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

    void loadTransactions() {
        view.showLoading();
        try {
            repository.getTransactions(selectedCategory, new Callback<List<Transaction>>() {
                @Override
                public void onSuccess(List<Transaction> result) {
                    view.showTransactions(result);
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

    void loadCategories() {
        try {
            categoryRepository.getCategories(new Callback<List<Category>>() {
                @Override
                public void onSuccess(List<Category> result) {
                    view.onLoadCategories(result);
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

    void btnAddTransactionClicked() {
        view.showAddTransactionBottomSheet();
    }

    void onTransactionClicked(Transaction transaction) {
        view.showModifyTransactionBottomSheet(transaction);
    }

    void setSelectedCategory(@Nullable Category category) {
        this.selectedCategory = category;
        loadTransactions();
    }
}
