package com.example.sharecipes.service;


import com.example.sharecipes.model.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RecipeServiceClient {

    /* Constants */
    private static final String TAG = "RecipeServiceClient";

    /* Data Members */
    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Boolean> mIsNetworkTimeout;

    /* Constructor */
    private RecipeServiceClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
        mIsNetworkTimeout = new MutableLiveData<>();
    }

    /* Singleton */
    private static RecipeServiceClient instance = null;
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

    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }
}
