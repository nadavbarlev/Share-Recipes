package com.example.sharecipes.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.repository.RecipeRepo;

import java.util.List;

public class RecipeListVM extends ViewModel {

    /* Constants */
    private static final String TAG = "RecipeListVM";

    /* Data Members */
    private RecipeRepo mRecipeRepo;

    /* Constructor */
    public RecipeListVM() {
        mRecipeRepo = RecipeRepo.getInstance();
    }

    /* Methods */
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepo.getRecipes();
    }

    public void searchRecipe(String query, int page) {
        mRecipeRepo.searchRecipe(query, page);
    }
}
