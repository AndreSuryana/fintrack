package com.andresuryana.fintrack.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.andresuryana.fintrack.ui.analytic.AnalyticFragment;
import com.andresuryana.fintrack.ui.category.CategoryFragment;
import com.andresuryana.fintrack.ui.dashboard.DashboardFragment;
import com.andresuryana.fintrack.ui.profile.ProfileFragment;
import com.andresuryana.fintrack.ui.transaction.TransactionFragment;

import java.util.ArrayList;
import java.util.List;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public static final int MENU_DASHBOARD = 0;
    public static final int MENU_TRANSACTIONS = 1;
    public static final int MENU_ANALYTICS = 2;
    public static final int MENU_CATEGORIES = 3;
    public static final int MENU_PROFILE = 4;
    public static final int MENU_COUNT = 5;

    private final List<Fragment> fragments = new ArrayList<>();

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        init();
    }

    private void init() {
        // Define fragment list
        fragments.add(MENU_DASHBOARD, new DashboardFragment());
        fragments.add(MENU_TRANSACTIONS, new TransactionFragment());
        fragments.add(MENU_ANALYTICS, new AnalyticFragment());
        fragments.add(MENU_CATEGORIES, new CategoryFragment());
        fragments.add(MENU_PROFILE, new ProfileFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return MENU_COUNT;
    }
}
