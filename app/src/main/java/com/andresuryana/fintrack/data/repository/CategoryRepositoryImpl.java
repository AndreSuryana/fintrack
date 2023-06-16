package com.andresuryana.fintrack.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.prefs.SessionHelperImpl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final DatabaseReference categoryRef;

    public CategoryRepositoryImpl(Context context) {
        SessionHelper session = new SessionHelperImpl(context);
        String userId = session.getCurrentUserId();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.categoryRef = db.getReference("users").child(userId).child("categories");
    }


    @Override
    public void addCategory(Category category, Callback<Category> callback) {
        try {
            // Push new category into the references
            categoryRef.push().getRef().setValue(category);

            callback.onSuccess(category);
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void removeCategory(Category category, Callback<Category> callback) {
        try {
            // Get and delete category references
            DatabaseReference deleteCategoryRef = categoryRef.child(category.getUid());
            deleteCategoryRef.removeValue((error, ref) -> {
                if (error == null) {
                    // Category removed successfully
                    callback.onSuccess(category);
                } else {
                    // Error occurred while removing the category
                    callback.onFailure(error.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void updateCategory(String categoryUid, Category category, Callback<Category> callback) {
        try {
            // Get the specific category reference
            DatabaseReference updatedCategoryRef = categoryRef.child(categoryUid);

            // Update the category object in the database
            updatedCategoryRef.setValue(category, (error, ref) -> {
                if (error == null) {
                    // Category updated successfully
                    callback.onSuccess(category);
                } else {
                    // Error occurred while updating the category
                    callback.onFailure(error.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void getCategories(Callback<List<Category>> callback) {
        try {
            // Retrieve the categories data from the database
            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Category> categories = new ArrayList<>();
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        Category category = categorySnapshot.getValue(Category.class);
                        if (category != null) {
                            category.setUid(categorySnapshot.getKey());
                            categories.add(category);
                        }
                    }
                    // Categories retrieved successfully
                    callback.onSuccess(categories);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error occurred while retrieving the categories
                    callback.onFailure(databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void insertCategoriesForNewUser(String userId) {
        try {
            // Push new category into the references
            Category.getDefaultCategories().forEach(category -> categoryRef.push().getRef().setValue(category));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
