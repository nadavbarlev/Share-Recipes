package com.example.sharecipes.viewmodel;

import android.app.Application;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.repository.RecipeRepo;
import com.example.sharecipes.util.Constants;
import com.example.sharecipes.util.network.Resource;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


public class RecipeListVM extends AndroidViewModel {

    /* Constants */
    private static final String TAG = "RecipeListVM";
    public static final String QUERY_EXHAUSTED = "No more results";

    /* Data Members */
    public enum ViewState { CATEGORIES, RECIPES }
    private RecipeRepo mRecipeRepo;
    private MutableLiveData<ViewState> mViewState;
    private MediatorLiveData<Resource<List<Recipe>>> mRecipes;
    private boolean mIsPerformQuery;
    private boolean mIsQueryExhausted;
    private String mQuery;
    private int    mPageNumber;

    /* Constructor */
    public RecipeListVM(@NonNull Application application) {
        super(application);
        setupViewState();

        mRecipes = new MediatorLiveData<>();
        mRecipeRepo = RecipeRepo.getInstance(application);
    }

    /* Private Methods */
    private void setupViewState() {
        if (mViewState != null) { return; }
        mViewState = new MutableLiveData();
        mViewState.setValue(ViewState.CATEGORIES);
    }

    private void executeSearch() {

        // Set perform query
        this.mIsPerformQuery = true;

        // Set display state to Recipes
        this.mViewState.setValue(ViewState.RECIPES);

        // Gets async recipes
        final LiveData<Resource<List<Recipe>>> repoRecipes = mRecipeRepo.searchRecipesApi(mQuery, mPageNumber);

        // Observe to recipes getting from server
        mRecipes.addSource(repoRecipes, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {

                // There is no data to observe
                if (listResource == null) {
                    mRecipes.removeSource(repoRecipes);
                    return;
                }

                // Post resource
                mViewState.setValue(ViewState.RECIPES);
                mRecipes.setValue(listResource);

                // Error occurred
                if (listResource.status == Resource.Status.ERROR) {
                    mIsPerformQuery = false;
                    mRecipes.removeSource(repoRecipes);
                }

                // Success
                else if (listResource.status == Resource.Status.SUCCESS) {
                    mIsPerformQuery = false;

                    if (listResource.data == null) {
                        mRecipes.removeSource(repoRecipes);
                        return;
                    }

                    if (listResource.data.size() == 0) {
                        mRecipes.setValue(Resource.error(QUERY_EXHAUSTED, listResource.data));
                        mRecipes.removeSource(repoRecipes);
                    }
                }
            }
        });
    }

    /* Public Methods */
    public LiveData<ViewState> getViewState() {
        return mViewState;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return mRecipes;
    }

    public void setViewState(ViewState state) {
        mViewState.setValue(state);
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

    public void searchRecipe(String query, int pageNumber) {
        if (!mIsPerformQuery) {
            this.mPageNumber = pageNumber;
            this.mQuery = query;
            mIsQueryExhausted = false;
            executeSearch();
        }
    }

    public void searchNextPage() {
        if (!mIsPerformQuery && !mIsQueryExhausted) {
            this.mPageNumber++;
            executeSearch();
        }
    }

    /*

    public LiveData<Boolean> getIsQueryExhausted() {
        return mRecipeRepo.getIsQueryExhausted();
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



