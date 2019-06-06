package com.example.sharecipes.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.repository.RecipeRepo;
import com.example.sharecipes.util.Constants;

import java.util.ArrayList;
import java.util.List;



public class RecipeListVM extends AndroidViewModel {

    /* Constants */
    private static final String TAG = "RecipeListVM";

    /* Data Members */
    public enum ViewState { CATEGORIES, RECIPES }
    private MutableLiveData<ViewState> mViewState;

    /* Constructor */
    public RecipeListVM(@NonNull Application application) {
        super(application);
        setupViewState();
    }

    /* Methods */
    private void setupViewState() {
        if (mViewState != null) { return; }
        mViewState = new MutableLiveData();
        mViewState.setValue(ViewState.CATEGORIES);
    }

    public LiveData<ViewState> getViewState() {
        return mViewState;
    }

    public List<Recipe> getCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int index = 0; index < Constants.DEFAULT_SEARCH_CATEGORIES.length; index++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[index]);
            recipe.setImage_url("android.resource://com.example.sharecipes/drawable/" +
                    Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[index]);
            categories.add(recipe);
        }
        return categories;
    }

    /*
    private RecipeRepo mRecipeRepo;
    private boolean    mIsRecipesDisplay;

    public RecipeListVM() {
        mRecipeRepo = RecipeRepo.getInstance();
        mIsRecipesDisplay = false;
    }
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepo.getRecipes();
    }

    public LiveData<Boolean> getIsQueryExhausted() {
        return mRecipeRepo.getIsQueryExhausted();
    }

    public List<Recipe> getCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int index = 0; index < Constants.DEFAULT_SEARCH_CATEGORIES.length; index++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[index]);
            recipe.setImage_url("android.resource://com.example.sharecipes/drawable/" +
                    Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[index]);
            categories.add(recipe);
        }
        return categories;
    }

    public void searchRecipe(String query, int page) {
        mIsRecipesDisplay = true;
        mRecipeRepo.searchRecipe(query, page);
    }

    public void searchNextPage() {
        if (!mIsRecipesDisplay || getIsQueryExhausted().getValue()) { return; }
        mRecipeRepo.searchNextPage();
    }

    public void setIsRecipesDisplay(boolean isDisplay) {
        mIsRecipesDisplay = isDisplay;
    }

    public boolean isRecipesDisplay() {
        return mIsRecipesDisplay;
    }
    */
}



