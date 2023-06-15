package com.andresuryana.fintrack.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

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
            binding.getRoot().setRefreshing(false);
        });

        // Request data
        presenter.loadTransactions();
        presenter.loadCategories();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup button
        setupButton();
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
        // TODO: Update chip list here!


        // Store categories in Fragment
        this.categories = categories;

        // Show add transaction button with fade animation
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(100L);
        binding.fabAddTransaction.setVisibility(View.VISIBLE);
        binding.fabAddTransaction.startAnimation(fadeInAnimation);
    }

    @Override
    public void showAddTransactionBottomSheet() {
        // Check categories
        if (categories == null) return;

        // Show bottom sheet add transaction
        TransactionFormBottomSheet addTransactionDialog = new TransactionFormBottomSheet(requireContext(), this.categories, new TransactionFormBottomSheet.OnAddResultCallback() {
            @Override
            public void onSuccess(Transaction.Type type, String title, long amount, @Nullable Category category, Date date, @Nullable String notes) {
                presenter.addTransaction(type, title, amount, date, notes, category);
            }

            @Override
            public void onFailed(String message) {
                showErrorMessage(message);
            }
        });
        if (!addTransactionDialog.isVisible()) {
            addTransactionDialog.show(getParentFragmentManager(), "AddTransactionBottomSheet");
        }
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
        if (!modifyTransactionDialog.isVisible()) {
            modifyTransactionDialog.show(getParentFragmentManager(), "AddTransactionBottomSheet");
        }
    }

    private void setupButton() {
        // Add button
        binding.fabAddTransaction.setOnClickListener(v -> presenter.btnAddTransactionClicked());
    }
}