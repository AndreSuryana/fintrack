package com.andresuryana.fintrack.ui.base.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.andresuryana.fintrack.databinding.FragmentLoadingDialogBinding;

public class LoadingDialogFragment extends AppCompatDialogFragment {

    // Layout binding
    private FragmentLoadingDialogBinding binding;

    private static final String TAG = "LoadingDialogFragment";

    public static void show(FragmentManager fragmentManager) {
        LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(fragmentManager, TAG);
    }

    public static void dismiss(FragmentManager fragmentManager) {
        LoadingDialogFragment dialog = (LoadingDialogFragment) fragmentManager.findFragmentByTag(TAG);
        if (dialog != null) {
            dialog.dismissAllowingStateLoss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout
        binding = FragmentLoadingDialogBinding.inflate(requireActivity().getLayoutInflater());

        // Create dialog
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }
}
