package com.andresuryana.fintrack.data.repository;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(String message);
}