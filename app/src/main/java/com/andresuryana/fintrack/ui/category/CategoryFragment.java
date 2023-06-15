package com.andresuryana.fintrack.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.databinding.FragmentCategoryBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;

import java.util.List;

public class CategoryFragment extends BaseFragment implements CategoryView {

    // Layout binding
    FragmentCategoryBinding binding;

    // Presenter
    CategoryPresenter presenter;

    // Adapter
    CategoryAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater);

        // Init presenter
        presenter = new CategoryPresenter(requireContext(), this);

        // Setup adapter & recycler view
        adapter = new CategoryAdapter(category -> presenter.onCategoryClicked(category));
        binding.rvCategories.setAdapter(adapter);

        // Setup refresh layout
        binding.getRoot().setOnRefreshListener(() -> {
            presenter.loadCategories();
            binding.getRoot().setRefreshing(false);
        });

        // Request data
        presenter.loadCategories();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup button
        setupButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void showCategories(List<Category> categories) {
        // Update list on category adapter
        adapter.setList(categories);
    }

    @Override
    public void showAddCategoryBottomSheet() {
        // Show bottom sheet add category
        CategoryFormBottomSheet addCategoryDialog = new CategoryFormBottomSheet(requireContext(), new CategoryFormBottomSheet.OnAddResultCallback() {
            @Override
            public void onSuccess(String iconName, String categoryName) {
                presenter.addCategory(categoryName, iconName);
            }

            @Override
            public void onFailed(String message) {
                showErrorMessage(message);
            }
        });
        if (!addCategoryDialog.isVisible()) {
            addCategoryDialog.show(getParentFragmentManager(), "AddCategoryBottomSheet");
        }
    }

    @Override
    public void showModifyCategoryBottomSheet(Category category) {
        // Show bottom sheet edit category
        CategoryFormBottomSheet modifyCategoryDialog = new CategoryFormBottomSheet(requireContext(), category, new CategoryFormBottomSheet.OnModifyResultCallback() {
            @Override
            public void onEdit(Category oldCategory, String iconName, String categoryName) {
                presenter.updateCategory(oldCategory, categoryName, iconName);
            }

            @Override
            public void onDelete(Category category) {
                presenter.removeCategory(category);
            }

            @Override
            public void onFailed(String message) {
                showErrorMessage(message);
            }
        });
        if (!modifyCategoryDialog.isVisible()) {
            modifyCategoryDialog.show(getParentFragmentManager(), "ModifyCategoryBottomSheet");
        }
    }

    private void setupButton() {
        // Add button
        binding.fabAddCategory.setOnClickListener(v -> presenter.btnAddCategoryClicked());
    }
}