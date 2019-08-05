package com.example.sharecipes.util.callback;

public interface GenericCallback<T,Z> {
    void onSuccess(T value);
    void onFailure(Z error);
}