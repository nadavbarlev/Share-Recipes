package com.example.sharecipes.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.service.Responses.RecipeSearchResponse;
import com.example.sharecipes.util.AppExecutors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sharecipes.util.Constants.NETWORK_TIMEOUT;
import static com.example.sharecipes.util.Constants.API_KEY;

public class RecipeServiceClient {

    /* Constants */
    private static final String TAG = "RecipeServiceClient";

    /* Data Members */
    private static RecipeServiceClient instance = null;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;

    /* Constructor */
    private RecipeServiceClient() {
        mRecipes = new MutableLiveData<>();
    }

    /* Singleton */
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

    public void searchRecipe(String query, int page) {

        // Create new instance
        if (mRetrieveRecipeRunnable != null) { mRetrieveRecipeRunnable = null; }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(query, page);

        // Execute Runnable
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipeRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() { handler.cancel(true); }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);

    }

    /* Runnable classes */
    private class RetrieveRecipeRunnable implements Runnable {

        /* Constants */
        private static final String TAG = "RetrieveRecipeRunnable";

        /* Data Members */
        private String mQuery;
        private int mPage;
        private boolean mIsCancelRequest;

        /* Constructor */
        public RetrieveRecipeRunnable(String query, int page) {
            this.mQuery = query;
            this.mPage = page;
            this.mIsCancelRequest = false;
        }

        @Override
        public void run() {

            if (mIsCancelRequest) { return; }

            try {

                // Execute Call Search Recipe Statement
                Call<RecipeSearchResponse> recipeSearchResponseCall =
                        ServiceGenerator.getRecipeService().searchRecipe(API_KEY, mQuery, String.valueOf(mPage));
                Response response = recipeSearchResponseCall.execute();

                // Success Code
                if (response.code() == 200) {
                    List<Recipe> recipes = new ArrayList<Recipe>(((RecipeSearchResponse)response.body()).getRecipes());
                    if (mPage == 1) {
                        mRecipes.postValue(recipes);
                    } else {
                        List<Recipe> currRecipes = mRecipes.getValue();
                        currRecipes.addAll(recipes);
                        mRecipes.postValue(currRecipes);
                    }
                }

                // Error Code
                else {
                    Log.e(TAG, "run: " + response.errorBody().string());
                    mRecipes.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        /* Methods */
        public void setCancelRequest(boolean isCancelRequest) {
            mIsCancelRequest = isCancelRequest;
        }
    }
}
