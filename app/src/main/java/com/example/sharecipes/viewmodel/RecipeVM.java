package com.example.sharecipes.viewmodel;

import com.example.sharecipes.repository.RecipeRepo;

import androidx.lifecycle.ViewModel;

public class RecipeVM extends ViewModel {

    /* Data Members */
    private String mCurrRecipeID;
    private RecipeRepo mRecipeRepo;
    private Boolean    mIsRetrieveRecipe = false;

    /* Constructor */
    public RecipeVM() {
        // mRecipeRepo = RecipeRepo.getInstance();
    }

    /* Getter and Setter */
    public void setIsRetrieveRecipe(Boolean isRetrieveRecipe) {
        mIsRetrieveRecipe = isRetrieveRecipe;
    }

    public Boolean getIsRetrieveRecipe() {
        return mIsRetrieveRecipe;
    }

    /* Methods */
    /*
    public LiveData<Recipe> getRecipe() {
        return mRecipeRepo.getRecipe();
    }

    public LiveData<Boolean> getIsNetworkTimeout() {
        return mRecipeRepo.getIsNetworkTimeout();
    }*/

    public String getRecipeID() {
        return mCurrRecipeID;
    }
/*
    public void searchRecipeBy(String id) {
        mCurrRecipeID = id;
        mRecipeRepo.searchRecipeBy(id);
    }*/
}
