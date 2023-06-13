package com.andresuryana.fintrack.data.repository;

import com.andresuryana.fintrack.data.model.Category;

import java.util.List;

public interface CategoryRepository {

    void addCategory(Category category, Callback<Category> callback);

    void removeCategory(Category category, Callback<Category> callback);

    void updateCategory(String categoryUid, Category category, Callback<Category> callback);

    void getCategories(Callback<List<Category>> callback);

    void insertCategoriesForNewUser(String userId);
}
