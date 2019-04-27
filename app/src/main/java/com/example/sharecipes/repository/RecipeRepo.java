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
}