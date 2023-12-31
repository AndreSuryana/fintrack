package com.andresuryana.fintrack.ui.dashboard;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.DashboardInfo;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.databinding.FragmentDashboardBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;
import com.andresuryana.fintrack.ui.transaction.TransactionAdapter;
import com.andresuryana.fintrack.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class DashboardFragment extends BaseFragment implements DashboardView {

    // Layout binding
    private FragmentDashboardBinding binding;

    // Presenter
    DashboardPresenter presenter;

    // Adapter
    TransactionAdapter adapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater);

        // Init presenter
        presenter = new DashboardPresenter(requireContext(), this);

        // Setup adapter & recycler view
        adapter = new TransactionAdapter(transaction -> {
        });
        binding.rvTransactions.setAdapter(adapter);

        // Greetings
        setGreetings();

        // Setup refresh layout
        binding.getRoot().setOnRefreshListener(() -> {
            presenter.loadDashboardInfo();
            presenter.loadLastTransactions();
            binding.getRoot().setRefreshing(false);
        });

        // Request data
        presenter.loadDashboardInfo();
        presenter.loadLastTransactions();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void onLoadDashboardInfo(DashboardInfo info) {
        // Format data
        String currencySymbol = "Rp";
        String incomeString = StringUtil.formatRupiah(info.getIncome()).replace(currencySymbol, "");
        String outcomeString = StringUtil.formatRupiah(info.getOutcome()).replace(currencySymbol, "");
        String remainderString = StringUtil.formatRupiah(Math.abs(info.getIncome() - info.getOutcome()));

        // Check remainder is minus or no
        if (info.getOutcome() > info.getIncome()) {
            // Remainder is minus
            remainderString = "-" + remainderString;
            binding.cardDashboard.tvRemainderAmount.setTextColor(ColorStateList.valueOf(requireContext().getColor(R.color.colorError)));
        }

        // Set dashboard info
        binding.cardDashboard.tvIncomeSymbol.setText(currencySymbol);
        binding.cardDashboard.tvIncomeAmount.setText(incomeString);
        binding.cardDashboard.tvOutcomeSymbol.setText(currencySymbol);
        binding.cardDashboard.tvOutcomeAmount.setText(outcomeString);
        binding.cardDashboard.tvRemainderAmount.setText(remainderString);
    }

    @Override
    public void onLoadLastTransaction(List<Transaction> transactions) {
        // Update list on category adapter
        adapter.setList(transactions);
    }

    private void setGreetings() {
        String userName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        if (userName != null) {
            binding.tvGreetingTitle.setText(getString(R.string.title_greet, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
        } else {
            binding.tvGreetingTitle.setText(getString(R.string.title_greet_alt));
        }
    }
}