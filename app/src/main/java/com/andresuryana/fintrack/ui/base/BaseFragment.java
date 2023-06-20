package com.andresuryana.fintrack.ui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.ui.base.dialog.LoadingDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment implements BaseView {

    // Loading dialog
    private LoadingDialogFragment loadingDialog;

    // Nav controller
    private NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init nav controller
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null || !loadingDialog.isAdded()) {
            loadingDialog = new LoadingDialogFragment(getParentFragmentManager(), getClass().getSimpleName());
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isAdded()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(getView().getContext(), R.color.colorError));
            snackbar.show();
        }
    }

    public NavController getNavController() {
        return navController;
    }
}
