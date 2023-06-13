package com.andresuryana.fintrack.ui.main;

import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_ANALYTICS;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_CATEGORIES;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_DASHBOARD;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_PROFILE;
import static com.andresuryana.fintrack.ui.main.MainViewPagerAdapter.MENU_TRANSACTIONS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    // Layout binding
    FragmentMainBinding binding;

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

        // Setup view pager
        setupViewPager();

        // Setup bottom navigation
        setupBottomNavigation();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
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
            } else if (itemId == R.id.categoryFragment) {
                setToolbarTitle(R.string.title_categories);
                binding.viewPagerMain.setCurrentItem(MENU_CATEGORIES);
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

    private void setToolbarTitle(@StringRes int titleStringRes) {
        binding.toolbar.setTitle(getString(titleStringRes));
    }
}