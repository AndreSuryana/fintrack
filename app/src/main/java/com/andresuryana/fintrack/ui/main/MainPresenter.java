package com.andresuryana.fintrack.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.data.repository.Callback;
import com.andresuryana.fintrack.data.repository.CategoryRepository;
import com.andresuryana.fintrack.data.repository.CategoryRepositoryImpl;
import com.andresuryana.fintrack.data.repository.TransactionRepository;
import com.andresuryana.fintrack.data.repository.TransactionRepositoryImpl;

import java.util.Date;
import java.util.List;

public class MainPresenter {

    private final Context context;
    private final MainView view;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public MainPresenter(Context context, MainView view) {
        this.context = context;
        this.view = view;
        this.categoryRepository = new CategoryRepositoryImpl(context);
        this.transactionRepository = new TransactionRepositoryImpl(context);
    }

    public void loadCategories() {
        try {
            categoryRepository.getCategories(new Callback<List<Category>>() {
                @Override
                public void onSuccess(List<Category> result) {
                    view.onCategoriesLoaded(result);
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

    void addTransaction(Transaction.Type type, String title, long amount, Date date, @Nullable String notes, Category category) {
        try {
            if (title.isEmpty() || amount == 0 || date == null) {
                view.showErrorMessage(context.getString(R.string.error_check_input));
            } else {
                Transaction transaction = new Transaction(type, title, amount, date, notes);
                if (category != null) {
                    transaction.setCategoryIconName(category.getIconName());
                    transaction.setCategoryName(category.getName());
                }
                transactionRepository.addTransaction(transaction, new Callback<Transaction>() {
                    @Override
                    public void onSuccess(Transaction result) {
                        view.showMessage(context.getString(R.string.success_add_transaction, transaction.getTitle()));
                        // TODO: Notify to the subscriber to refresh the list
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

    void btnAddTransactionClicked() {
        view.showAddTransactionBottomSheet();
    }
}
