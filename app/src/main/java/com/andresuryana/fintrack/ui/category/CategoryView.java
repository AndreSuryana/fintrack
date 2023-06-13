package com.andresuryana.fintrack.ui.category;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.ui.base.BaseView;

import java.util.List;

public interface CategoryView extends BaseView {

    void showCategories(List<Category> categories);

    void showAddCategoryBottomSheet();

    void showModifyCategoryBottomSheet(Category category);
}
