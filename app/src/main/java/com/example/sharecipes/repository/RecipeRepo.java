package com.example.sharecipes.repository;

import android.arch.lifecycle.LiveData;
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

    /* Constructor */
    private RecipeRepo() {
        mRecipeServiceClient = RecipeServiceClient.getInstance();
    }

    /* Singleton */
    public static RecipeRepo getInstance() {
        if (instance == null) {
            instance = new RecipeRepo();
        }
        return instance;
    }

    /* Methods */
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeServiceClient.getRecipes();
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
        mRecipeServiceClient.searchRecipe(query, page);
    }

    public void searchRecipeBy(String id) {
        mRecipeServiceClient.searchRecipeBy(id);
    }

    public void searchNextPage() {
        searchRecipe(mQuery, mPage + 1);
    }
}