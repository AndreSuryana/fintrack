package com.andresuryana.fintrack.ui.base;

import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.andresuryana.fintrack.R;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment implements BaseView {

    // Progress bar
    private ProgressBar progressBar;

    @Override
    public void showLoading() {
        if (progressBar == null) {
            progressBar = new ProgressBar(getActivity());
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
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
}
