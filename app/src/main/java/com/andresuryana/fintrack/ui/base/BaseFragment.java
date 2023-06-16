package com.andresuryana.fintrack.ui.base;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.ui.base.dialog.LoadingDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment implements BaseView {

    @Override
    public void showLoading() {
        LoadingDialogFragment.show(getParentFragmentManager());
    }

    @Override
    public void hideLoading() {
        LoadingDialogFragment.dismiss(getParentFragmentManager());
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
}
