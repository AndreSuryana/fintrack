package com.andresuryana.fintrack.ui.category;

import android.content.Context;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.repository.Callback;
import com.andresuryana.fintrack.data.repository.CategoryRepository;
import com.andresuryana.fintrack.data.repository.CategoryRepositoryImpl;

import java.util.List;

public class CategoryPresenter {

    private final Context context;
    private final CategoryView view;
    private final CategoryRepository repository;

    public CategoryPresenter(Context context, CategoryView view) {
        this.context = context;
        this.view = view;
        this.repository = new CategoryRepositoryImpl(context);
    }

    public void addCategory(String name, String iconName) {
        try {
            if (name.isEmpty()) {
                view.showErrorMessage(context.getString(R.string.helper_empty_category_name));
            } else {
                Category category = new Category(name, iconName);
                repository.addCategory(category, new Callback<Category>() {
                    @Override
                    public void onSuccess(Category result) {
                        view.showMessage(context.getString(R.string.success_add_category, result.getName()));
                        loadCategories();
                    }

                    @Override
                    public void onFailure(String message) {
                        view.showErrorMessage(message);
                    }
                });
            }
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void updateCategory(Category oldCategory, String newName, String newIconName) {
        try {
            Category newCategory = new Category(oldCategory.getUid(), newName, newIconName);
            repository.updateCategory(oldCategory.getUid(), newCategory, new Callback<Category>() {
                @Override
                public void onSuccess(Category result) {
                    view.showMessage(context.getString(R.string.success_update_category, result.getName()));
                    loadCategories();
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void removeCategory(Category category) {
        try {
            repository.removeCategory(category, new Callback<Category>() {
                @Override
                public void onSuccess(Category result) {
                    view.showMessage(context.getString(R.string.success_remove_category, result.getName()));
                    loadCategories();
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    void loadCategories() {
        view.showLoading();
        try {
            repository.getCategories(new Callback<List<Category>>() {
                @Override
                public void onSuccess(List<Category> result) {
                    view.showCategories(result);
                    view.hideLoading();
                }

                @Override
                public void onFailure(String message) {
                    view.showErrorMessage(message);
                    view.hideLoading();
                }
            });
        } catch (Exception e) {
            view.showErrorMessage(e.getMessage());
            view.hideLoading();
        }
    }

    void btnAddCategoryClicked() {
        view.showAddCategoryBottomSheet();
    }

    void onCategoryClicked(Category category) {
        view.showModifyCategoryBottomSheet(category);
    }
}
