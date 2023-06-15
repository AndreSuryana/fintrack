package com.andresuryana.fintrack.data.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.ServerValue;

import java.util.Date;

public class Transaction {

    private String uid;
    private Type type;
    private String title;
    private long amount;
    private Date date;
    private String notes;
    private String categoryName;
    private String categoryIconName;
    private Object timestamp;

    public Transaction() {
        // Default constructors
    }

    public Transaction(Type type, String title, long amount, Date date, String notes /* String categoryName, String categoryIconName */) {
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
//        this.categoryName = categoryName;
//        this.categoryIconName = categoryIconName;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public enum Type {
        INCOME {
            @NonNull
            @Override
            public String toString() {
                return "Income";
            }
        },
        OUTCOME {
            @NonNull
            @Override
            public String toString() {
                return "Outcome";
            }
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategoryIconName() {
        return categoryIconName;
    }

    public void setCategoryIconName(String categoryIconName) {
        this.categoryIconName = categoryIconName;
    }
}
