package com.andresuryana.fintrack.ui.main;

import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_ANALYTICS;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_DASHBOARD;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_PROFILE;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_TRANSACTIONS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.databinding.FragmentMainBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;
import com.andresuryana.fintrack.ui.transaction.TransactionFormBottomSheet;

import java.util.List;

public class MainFragment extends BaseFragment implements MainView {

    // Layout binding
    private FragmentMainBinding binding;

    // Presenter
    private MainPresenter presenter;

    private List<Category> categories;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater);

        // Init presenter
        presenter = new MainPresenter(requireContext(), this);

        // Setup view pager
        setupViewPager();

        // Setup bottom navigation
        setupBottomNavigation();

        // Setup button
        setupButton();

        // Request data
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
    public void onCategoriesLoaded(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void showAddTransactionBottomSheet() {
        // Check categories
        if (categories == null) return;

        // Show bottom sheet add transaction
        TransactionFormBottomSheet addTransactionDialog = new TransactionFormBottomSheet(requireContext(), this.categories, (type, title, amount, category, date, notes) -> {
            // Add transaction
            presenter.addTransaction(type, title, amount, date, notes, category);
        });
        if (!addTransactionDialog.isVisible()) {
            addTransactionDialog.show(getParentFragmentManager(), "AddTransactionBottomSheet");
        }
    }

    private void setupViewPager() {
        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(requireActivity());
        binding.viewPagerMain.setAdapter(viewPagerAdapter);
        binding.viewPagerMain.setUserInputEnabled(false);
    }

    private void setupBottomNavigation() {
        // Add on bottom nav item click changes
        binding.bottomNav.setOnItemSelectedListener(item -> {
            // Get item id
            int itemId = item.getItemId();

            // Update layout according to it's menu id
            if (itemId == R.id.dashboardFragment) {
                setToolbarTitle(R.string.title_dashboard);
                binding.viewPagerMain.setCurrentItem(MENU_DASHBOARD);
                return true;
            } else if (itemId == R.id.transactionFragment) {
                setToolbarTitle(R.string.title_transactions);
                binding.viewPagerMain.setCurrentItem(MENU_TRANSACTIONS);
                return true;
            } else if (itemId == R.id.analyticFragment) {
                setToolbarTitle(R.string.title_analytics);
                binding.viewPagerMain.setCurrentItem(MENU_ANALYTICS);
                return true;
            } else if (itemId == R.id.profileFragment) {
                setToolbarTitle(R.string.title_profile);
                binding.viewPagerMain.setCurrentItem(MENU_PROFILE);
                return true;
            } else {
                setToolbarTitle(R.string.title_dashboard);
                binding.viewPagerMain.setCurrentItem(MENU_DASHBOARD);
                return true;
            }
        });
    }

    private void setupButton() {
        // Button add transaction
        binding.fabAddTransaction.setOnClickListener((view) -> presenter.btnAddTransactionClicked());
    }

    private void setToolbarTitle(@StringRes int titleStringRes) {
        binding.toolbar.setTitle(getString(titleStringRes));
    }
}