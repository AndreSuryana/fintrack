package com.andresuryana.fintrack.data.repository;

import com.andresuryana.fintrack.data.model.Category;

import java.util.List;

public interface CategoryRepository {

    void addCategory(Category category, Callback<Void> callback);

    void removeCategory(Category category, Callback<Void> callback);

    void updateCategory(String categoryUid, Category category, Callback<Void> callback);

    void getCategories(Callback<List<Category>> callback);

    void insertCategoriesForNewUser(String userId);

    interface Callback<T> {
        void onSuccess();
        void onSuccess(T result);
        void onFailure(String message);
    }
}
