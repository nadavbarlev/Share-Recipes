package com.example.sharecipes.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.service.RecipeServiceClient;
import java.util.List;

public class RecipeRepo {

    /* Constants */
    private static final String TAG = "RecipeRepo";

    /* Data Members */
    private static RecipeRepo instance = null;
    private RecipeServiceClient mRecipeServiceClient;
    private String mQuery;
    private int mPage;
    private MutableLiveData<Boolean> mIsQueryExhausted;
    private MediatorLiveData<List<Recipe>> mMediatorRecipes;

    /* Constructor */
    private RecipeRepo() {
        mRecipeServiceClient = RecipeServiceClient.getInstance();
        mIsQueryExhausted = new MutableLiveData<>();
        setupMediators();
    }

    /* Singleton */
    public static RecipeRepo getInstance() {
        if (instance == null) {
            instance = new RecipeRepo();
        }
        return instance;
    }

    /* Private Methods */
    private void setupMediators() {
        mMediatorRecipes = new MediatorLiveData<>();
        mMediatorRecipes.addSource(mRecipeServiceClient.getRecipes(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null) {
                    mIsQueryExhausted.setValue(true);
                    return;
                }
                mMediatorRecipes.setValue(recipes);
                if (recipes.size() % 30 != 0) { mIsQueryExhausted.setValue(true); }
            }
        });
    }

    /* Public Methods */
    public LiveData<Boolean> getIsQueryExhausted() {
        return mIsQueryExhausted;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mMediatorRecipes;
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeServiceClient.getRecipe();
    }

    public LiveData<Boolean> getIsNetworkTimeout() {
        return mRecipeServiceClient.getIsNetworkTimeout();
    }

    public void searchRecipe(String query, int page) {
        mQuery = query;
        mPage = page;
        mIsQueryExhausted.setValue(false);
        mRecipeServiceClient.searchRecipe(query, page);
    }

    public void searchRecipeBy(String id) {
        mRecipeServiceClient.searchRecipeBy(id);
    }

    public void searchNextPage() {
        searchRecipe(mQuery, mPage + 1);
    }
}