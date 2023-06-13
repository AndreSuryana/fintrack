package com.andresuryana.fintrack.data.model;

import com.andresuryana.fintrack.R;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String uid;
    private String name;
    private int iconRes;

    public Category() {
        // Default constructor
    }

    public Category(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public Category(String uid, String name, int iconRes) {
        this.uid = uid;
        this.name = name;
        this.iconRes = iconRes;
    }

    public static List<Category> getDefaultCategories() {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Food & Dining", R.drawable.ic_local_dining));
        categories.add(new Category("Transportation", R.drawable.ic_directions_car));
        categories.add(new Category("House Expenses", R.drawable.ic_house));
        categories.add(new Category("Shopping", R.drawable.ic_shopping_cart));
        categories.add(new Category("Education", R.drawable.ic_school));

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

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
