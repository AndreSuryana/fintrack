package com.andresuryana.fintrack.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.databinding.FragmentTransactionBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;

import java.util.Date;
import java.util.List;

public class TransactionFragment extends BaseFragment implements TransactionView {

    // Layout binding
    private FragmentTransactionBinding binding;

    // Presenter
    private TransactionPresenter presenter;

    // Adapter
    private TransactionAdapter adapter;

    // List of category
    private List<Category> categories;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater);

        // Init presenter
        presenter = new TransactionPresenter(requireContext(), this);

        // Setup adapter & recycler view
        adapter = new TransactionAdapter(transaction -> presenter.onTransactionClicked(transaction));
        binding.rvTransactions.setAdapter(adapter);

        // Setup refresh layout
        binding.getRoot().setOnRefreshListener(() -> {
            presenter.loadTransactions();
            presenter.loadCategories();
            binding.getRoot().setRefreshing(false);
        });

        // Request data
        presenter.loadTransactions();
        presenter.loadCategories();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {
        // Update list on transaction adapter
        adapter.setList(transactions);
    }

    @Override
    public void onLoadCategories(List<Category> categories) {
        // Store categories in Fragment
        this.categories = categories;

        // TODO: Update chip list here!
    }

    @Override
    public void showModifyTransactionBottomSheet(Transaction transaction) {
        // Check categories
        if (categories == null) return;

        // Show bottom sheet add transaction
        TransactionFormBottomSheet modifyTransactionDialog = new TransactionFormBottomSheet(requireContext(), transaction, this.categories, new TransactionFormBottomSheet.OnModifyResultCallback() {
            @Override
            public void onEdit(Transaction oldTransaction, Transaction.Type type, String title, long amount, @Nullable Category category, Date date, @Nullable String notes) {
                presenter.updateTransaction(oldTransaction, type, title, amount, date, notes, category);
            }

            @Override
            public void onDelete(Transaction transaction) {
                presenter.removeTransaction(transaction);
            }

            @Override
            public void onFailed(String message) {
                showErrorMessage(message);
            }
        });

        // Add listener for adding new category
        modifyTransactionDialog.setOnAddCategoryCallback((iconName, categoryName) -> presenter.addCategory(categoryName, iconName));

        if (!modifyTransactionDialog.isVisible()) {
            modifyTransactionDialog.show(getParentFragmentManager(), "AddTransactionBottomSheet");
        }
    }
}