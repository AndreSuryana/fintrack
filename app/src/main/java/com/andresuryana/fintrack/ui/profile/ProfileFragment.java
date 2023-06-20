package com.andresuryana.fintrack.ui.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavOptions;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.databinding.FragmentProfileBinding;
import com.andresuryana.fintrack.databinding.ItemSettingBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;
import com.andresuryana.fintrack.ui.profile.setting.MenuSetting;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

public class ProfileFragment extends BaseFragment implements ProfileView {

    // Layout binding
    private FragmentProfileBinding binding;

    // Presenter
    private ProfilePresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater);

        // Init presenter
        presenter = new ProfilePresenter(requireContext(), this);

        // Inflate menu
        inflateMenu();

        // Setup refresh layout
        binding.getRoot().setOnRefreshListener(() -> {
            presenter.loadUser();
            binding.getRoot().setRefreshing(false);
        });

        // Request data
        presenter.loadUser();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void onLoadUser(User user) {
        binding.tvProfileName.setText(user.getName());
        binding.tvProfileEmail.setText(user.getEmail());
        Glide.with(requireContext())
                .load(user.getImageUrl())
                .into(binding.ivProfile);
    }

    @Override
    public void onSuccessLogout() {
        // Navigate to login
        NavOptions navOptions = new NavOptions.Builder().setLaunchSingleTop(true).build();
        getNavController().navigate(R.id.loginFragment, null, navOptions);
    }

    private void inflateMenu() {
        // Loop through parent menu, and then menu item
        for (MenuSetting.Parent parent : MenuSetting.Parent.values()) {
            // Add menu parent title
            addMenuTitleView(parent.getTitle(), parent.ordinal() == 0);

            // Loop through menu item in the parent layout
            for (MenuSetting menu : MenuSetting.values()) {
                if (menu.getParent() == parent) {
                    // Inflate layout for menu
                    ItemSettingBinding menuBinding = ItemSettingBinding.inflate(getLayoutInflater());

                    // Set menu data
                    menuBinding.ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), menu.getIcon()));
                    menuBinding.tvTitle.setText(menu.getTitle());
                    menuBinding.getRoot().setOnClickListener((view) -> onMenuItemClickListener(menu));

                    // Add menu into menu container
                    binding.settingMenuContainer.addView(menuBinding.getRoot());
                }
            }
        }
    }

    private void addMenuTitleView(@StringRes Integer title, Boolean isFirstItem) {
        // Create a new LinearLayout instance as a wrapper
        LinearLayout wrapperLayout = new LinearLayout(requireContext());
        wrapperLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        wrapperLayout.setOrientation(LinearLayout.VERTICAL);
        int marginHorizontal = getResources().getDimensionPixelSize(R.dimen.margin_normal);
        int marginTop = getResources().getDimensionPixelSize(isFirstItem ? R.dimen.margin_small : R.dimen.margin_normal);
        wrapperLayout.setPadding(marginHorizontal, marginTop, marginHorizontal, 0);

        // Create a new MaterialTextView instance
        MaterialTextView textView = new MaterialTextView(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(layoutParams);
        textView.setText(title);
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        // Add the TextView to the wrapper layout
        wrapperLayout.addView(textView);

        // Add the wrapper layout to the parent view
        binding.settingMenuContainer.addView(wrapperLayout);
    }

    private void onMenuItemClickListener(MenuSetting menu) {
        switch (menu) {
            case MANAGE_CATEGORIES: {
                // Navigate to
                getNavController().navigate(R.id.action_mainFragment_to_categoryFragment);
                break;
            }
            case PERSONAL_DATA: {
                // TODO: Not yet implemented!
                Toast.makeText(requireContext(), "Not implemented yet!", Toast.LENGTH_SHORT).show();
                presenter.updatePersonalData();
                break;
            }
            case FORGOT_PASSWORD: {
                presenter.forgotPassword();
                break;
            }
            case LOGOUT: {
                presenter.logout();
                break;
            }
            default: {
                // Nothing
                break;
            }
        }
    }
}