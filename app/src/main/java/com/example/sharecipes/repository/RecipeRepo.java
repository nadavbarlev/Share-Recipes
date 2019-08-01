package com.example.sharecipes.repository;

import android.content.Context;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.presistence.RecipeDao;
import com.example.sharecipes.presistence.RecipeDatabase;
import com.example.sharecipes.service.Responses.RecipeSearchResponse;
import com.example.sharecipes.service.ServiceGenerator;
import com.example.sharecipes.util.AppExecutors;
import com.example.sharecipes.util.Constants;
import com.example.sharecipes.util.network.ApiResponse;
import com.example.sharecipes.util.network.NetworkBoundResource;
import com.example.sharecipes.util.network.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class RecipeRepo {

    /* Constants */
    private static final String TAG = "RecipeRepo";

    /* Data Members */
    private RecipeDao recipeDao;

    /* Constructor */
    private RecipeRepo(Context context) {
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
                if (item.getRecipes() == null) { return; }

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
}