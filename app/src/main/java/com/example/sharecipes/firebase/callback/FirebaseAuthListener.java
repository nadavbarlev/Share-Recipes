package com.example.sharecipes.firebase.callback;

public interface FirebaseAuthListener {
    void onSuccess(String userID);
    void onFailure(String errorMsg);
}