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

    private final FragmentManager fragmentManager;
    private final String tag;

    public LoadingDialogFragment(FragmentManager fragmentManager, String tag) {
        this.fragmentManager = fragmentManager;
        this.tag = tag + "Loading";
    }

    public void show() {
        this.setCancelable(false);
        this.show(fragmentManager, tag);
    }

    public void dismiss() {
        this.dismissAllowingStateLoss();
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
