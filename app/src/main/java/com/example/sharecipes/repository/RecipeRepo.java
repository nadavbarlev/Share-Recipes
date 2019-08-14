package com.example.sharecipes.repository;

import android.content.Context;

import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.presistence.RecipeDao;
import com.example.sharecipes.presistence.RecipeDatabase;
import com.example.sharecipes.service.Responses.RecipeResponse;
import com.example.sharecipes.service.Responses.RecipeSearchResponse;
import com.example.sharecipes.service.ServiceGenerator;
import com.example.sharecipes.util.AppExecutors;
import com.example.sharecipes.util.Constants;
import com.example.sharecipes.util.callback.GenericCallback;
import com.example.sharecipes.util.network.ApiResponse;
import com.example.sharecipes.util.network.NetworkBoundResource;
import com.example.sharecipes.util.network.NetworkHelper;
import com.example.sharecipes.util.network.Resource;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class RecipeRepo {

    /* Constants */
    private static final String TAG = "RecipeRepo";

    /* Data Members */
    private RecipeDao recipeDao;
    private Context mContext;

    /* Constructor */
    private RecipeRepo(Context context) {
        mContext = context;
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    /* Singleton */
    private static RecipeRepo instance = null;
    public static RecipeRepo getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRepo(context);
        }
        return instance;
    }

    /* Public Methods */
    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber) {

        // Implement the Single Source of Truth Principal
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {

                // Check if API Key is expired
                if (item.getRecipes() == null) {
                    return;
                }

                // Recipes got from Server
                Recipe[] recipes = new Recipe[item.getCount()];
                item.getRecipes().toArray(recipes);

                // Save to Cache
                long[] rowIDs = recipeDao.insertRecipes(recipes);
                for (int index = 0; index < rowIDs.length; index++) {

                    // Recipe already exists
                    if (rowIDs[index] == -1) {

                        // Don't change the ingredients and timestamp
                        Recipe recipe = recipes[index];
                        recipeDao.updateRecipe(recipe.getRecipe_id(),
                                recipe.getTitle(),
                                recipe.getPublisher(),
                                recipe.getSocial_rank(),
                                recipe.getImage_url());
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipe(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {

                return ServiceGenerator.getRecipeService()
                        .searchRecipe(Constants.API_KEY_2,
                                query,
                                String.valueOf(pageNumber));

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(final String recipeID) {

        return new NetworkBoundResource<Recipe, RecipeResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull RecipeResponse item) {

                // Check if API Key is expired
                if (item.getRecipe() == null) {
                    return;
                }

                // Recipes got from Server
                item.getRecipe().setTimestamp((int) System.currentTimeMillis() / 1000);
                recipeDao.insertRecipe(item.getRecipe());
            }

            @Override
            protected boolean shouldFetch(@Nullable Recipe data) {

                Boolean isNetworkAvailable = NetworkHelper.isNetworkAvailable(mContext);

                int currentTime = (int)System.currentTimeMillis() / 1000;
                int lastRecipeUpdateTime = data.getTimestamp();
                Boolean isTimeExpired = currentTime - lastRecipeUpdateTime > Constants.RECIPE_REFRESH_TIME;

                // return isNetworkAvailable && isTimeExpired;
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Recipe> loadFromDb() {
                LiveData<Recipe> recipe = recipeDao.getRecipe(recipeID);
                return recipe;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeResponse>> createCall() {
                return ServiceGenerator.getRecipeService().getRecipe(Constants.API_KEY_2, recipeID);
            }
        }.getAsLiveData();
    }

    public void getRecipesFB(String query, final GenericCallback<Recipe, String> callback) {

        FirebaseDatabaseService.getInstance().contains("recipes", "title", query,
                new GenericCallback<Map<String, String>, String>() {
                    @Override
                    public void onSuccess(Map<String, String> value) {
                        final Recipe recipe = Recipe.toRecipe(value);

                        AppExecutors.getInstance().background().execute(new Runnable() {
                            @Override
                            public void run() {
                                recipeDao.insertRecipe(recipe);
                            }
                        });

                        callback.onSuccess(recipe);
                    }

                    @Override
                    public void onFailure(String error) {
                        callback.onFailure(error);
                    }
                });

    }

    public void deleteCache() {
        AppExecutors.getInstance().background().execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.deleteAll();
            }
        });
    }
}