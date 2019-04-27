package com.example.sharecipes.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.example.sharecipes.model.Recipe;
import java.util.List;

public class RecipeServiceClient {

    /* Constants */
    private static final String TAG = "RecipeServiceClient";

    /* Data Members */
    private static RecipeServiceClient instance = null;
    private MutableLiveData<List<Recipe>> mRecipes;

    /* Constructor */
    private RecipeServiceClient() {
        mRecipes = new MutableLiveData<>();
    }

    /* Singleton */
    public static RecipeServiceClient getInstance() {
        if (instance == null) {
            instance = new RecipeServiceClient();
        }
        return instance;
    }

    /* Methods */
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
}
