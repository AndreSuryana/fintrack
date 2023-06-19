package com.andresuryana.fintrack.ui.main;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.ui.base.BaseView;

import java.util.List;

public interface MainView extends BaseView {

    void onCategoriesLoaded(List<Category> categories);

    void showAddTransactionBottomSheet();
}
