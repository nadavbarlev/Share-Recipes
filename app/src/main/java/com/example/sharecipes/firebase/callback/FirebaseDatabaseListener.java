package com.example.sharecipes.firebase.callback;

import java.util.Map;

public interface FirebaseDatabaseListener {
    void onSuccess(Map<String, String> mapRecipe);
    void onFailure();
}