package com.example.sharecipes.viewmodel;

import android.app.Application;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.repository.RecipeRepo;
import com.example.sharecipes.util.network.Resource;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RecipeVM extends AndroidViewModel {

    /* Data Members */
    private String mCurrRecipeID;
    private RecipeRepo mRecipeRepo;
    private Boolean    mIsRetrieveRecipe = false;

    /* Constructor */

    public RecipeVM(@NonNull Application application) {
        super(application);
        mRecipeRepo = RecipeRepo.getInstance(getApplication());
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

    public LiveData<Resource<Recipe>> searchRecipeBy(String id) {
        return mRecipeRepo.searchRecipeApi(id);
    }
}
