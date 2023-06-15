package com.andresuryana.fintrack.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String uid;
    private String name;
    private String iconName;

    public Category() {
        // Default constructor
    }

    public Category(String name, String iconName) {
        this.name = name;
        this.iconName = iconName;
    }

    public Category(String uid, String name, String iconName) {
        this.uid = uid;
        this.name = name;
        this.iconName = iconName;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    public static List<Category> getDefaultCategories() {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Food & Dining", "ic_local_dining"));
        categories.add(new Category("Transportation", "ic_directions_car"));
        categories.add(new Category("House Expenses", "ic_house"));
        categories.add(new Category("Shopping", "ic_shopping_cart"));
        categories.add(new Category("Education", "ic_school"));

        return categories;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
