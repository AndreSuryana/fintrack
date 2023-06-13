package com.andresuryana.fintrack.ui.category;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.databinding.BottomSheetAddCategoryBinding;
import com.andresuryana.fintrack.util.StringUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryFormBottomSheet extends BottomSheetDialogFragment {

    // Layout binding
    BottomSheetAddCategoryBinding binding;

    // Layout state
    private final LayoutState layoutState;

    // List of icons
    private final List<Integer> iconResources;

    // Adapter
    IconSpinnerAdapter iconAdapter;

    // Current category (if available)
    Category category;

    // Callback
    private OnAddResultCallback onAddResultCallback;
    private OnModifyResultCallback onModifyResultCallback;

    public CategoryFormBottomSheet(Context context, OnAddResultCallback onAddResultCallback) {
        this.iconResources = getIconResources();
        this.onAddResultCallback = onAddResultCallback;
        this.layoutState = LayoutState.ADD_CATEGORY;
        this.iconAdapter = new IconSpinnerAdapter(context, iconResources);
    }

    public CategoryFormBottomSheet(Context context, Category category, OnModifyResultCallback onModifyResultCallback) {
        this.iconResources = getIconResources();
        this.category = category;
        this.onModifyResultCallback = onModifyResultCallback;
        this.layoutState = LayoutState.MODIFY_CATEGORY;
        this.iconAdapter = new IconSpinnerAdapter(context, iconResources);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout binding
        binding = BottomSheetAddCategoryBinding.inflate(inflater);

        // Setup icon spinner
        binding.spinnerIcon.setAdapter(iconAdapter);

        // Setup text input layout
        setupTextInput();

        // Check layout state
        switch (layoutState) {
            default:
            case ADD_CATEGORY: {
                initAddCategory();
                break;
            }
            case MODIFY_CATEGORY: {
                initModifyCategory();
                break;
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    private void initAddCategory() {
        // Set title
        binding.tvTitle.setText(getString(R.string.title_add_category));

        // Show add category button
        binding.btnAddCategory.setVisibility(View.VISIBLE);

        // Button add category
        binding.btnAddCategory.setOnClickListener(view -> {
            // Check for error
            boolean isCategoryNameError = binding.tilCategoryName.getError() != null;

            // Get value
            Integer iconRes = iconAdapter.getItem(binding.spinnerIcon.getSelectedItemPosition());
            String categoryName = Objects.requireNonNull(binding.etCategoryName.getText()).toString();

            if (!isCategoryNameError && iconRes != null) {
                onAddResultCallback.onSuccess(iconRes, categoryName);
                dismiss();
            } else {
                onAddResultCallback.onFailed(getString(R.string.error_check_input));
                dismiss();
            }
        });

        // Hide modify button container
        binding.btnModifyContainer.setVisibility(View.GONE);
    }

    private void initModifyCategory() {
        // Set title
        binding.tvTitle.setText(getString(R.string.title_modify_category));

        // Prefill category data
        if (category != null) {
            binding.spinnerIcon.setSelection(iconAdapter.getPosition(category.getIconRes()));
            binding.etCategoryName.setText(category.getName());
        }

        // Show modify button container
        binding.btnModifyContainer.setVisibility(View.VISIBLE);

        // Button edit category
        binding.btnEdit.setOnClickListener(view -> {
            // Check for error
            boolean isCategoryNameError = binding.tilCategoryName.getError() != null;

            // Get value
            Integer iconRes = iconAdapter.getItem(binding.spinnerIcon.getSelectedItemPosition());
            String categoryName = Objects.requireNonNull(binding.etCategoryName.getText()).toString();

            if (!isCategoryNameError && iconRes != null) {
                onModifyResultCallback.onEdit(category, iconRes, categoryName);
                dismiss();
            } else {
                onModifyResultCallback.onFailed(getString(R.string.error_check_input));
                dismiss();
            }
        });

        // Button delete category
        binding.btnDelete.setOnClickListener(view -> {
            if (category != null) {
                onModifyResultCallback.onDelete(category);
                dismiss();
            } else {
                onModifyResultCallback.onFailed(getString(R.string.error_category_not_found));
                dismiss();
            }
        });

        // Hide add category button
        binding.btnAddCategory.setVisibility(View.GONE);
    }

    private void setupTextInput() {
        // Category name
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameString = s.toString();
                if (nameString.isEmpty()) {
                    // Empty user_name
                    binding.tilCategoryName.setError(getString(R.string.helper_empty_category_name));
                } else {
                    if (StringUtil.containsOnlyLettersWithSpaces(nameString)) {
                        // Valid user_name
                        binding.tilCategoryName.setError(null);
                    } else {
                        // Invalid user_name
                        binding.tilCategoryName.setError(getString(R.string.helper_invalid_category_name));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        };

        binding.etCategoryName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.etCategoryName.addTextChangedListener(textWatcher);
            } else {
                binding.etCategoryName.removeTextChangedListener(textWatcher);
            }
        });
    }

    private List<Integer> getIconResources() {
        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.ic_attach_money);
        icons.add(R.drawable.ic_local_dining);
        icons.add(R.drawable.ic_directions_car);
        icons.add(R.drawable.ic_shopping_cart);
        icons.add(R.drawable.ic_house);
        icons.add(R.drawable.ic_school);
        icons.add(R.drawable.ic_account_circle);
        icons.add(R.drawable.ic_fitness_center);
        icons.add(R.drawable.ic_flight);
        icons.add(R.drawable.ic_work);
        return icons;
    }

    public interface OnAddResultCallback {
        void onSuccess(Integer iconRes, String categoryName);
        void onFailed(String message);
    }

    public interface OnModifyResultCallback {
        void onEdit(Category oldCategory, Integer iconRes, String categoryName);
        void onDelete(Category category);
        void onFailed(String message);
    }

    private enum LayoutState {
        ADD_CATEGORY,
        MODIFY_CATEGORY
    }
}
