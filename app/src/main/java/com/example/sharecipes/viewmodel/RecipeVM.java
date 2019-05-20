package com.example.sharecipes.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.repository.RecipeRepo;

public class RecipeVM extends ViewModel {

    /* Data Members */
    private RecipeRepo mRecipeRepo;

    /* Constructor */
    public RecipeVM() {
        mRecipeRepo = RecipeRepo.getInstance();
    }

    /* Methods */
    public LiveData<Recipe> getRecipe() {
        return mRecipeRepo.getRecipe();
    }

    public void searchRecipeBy(String id) {
        mRecipeRepo.searchRecipeBy(id);
    }
}
