package com.example.sharecipes.viewmodel;

import android.app.Application;

import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.presistence.RecipeDao;
import com.example.sharecipes.presistence.RecipeDatabase;
import com.example.sharecipes.repository.RecipeRepo;
import com.example.sharecipes.util.AppExecutors;
import com.example.sharecipes.util.callback.GenericCallback;
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

    public String getRecipeID() {
        return mCurrRecipeID;
    }

    public void deleteRecipe(final Recipe recipe, final GenericCallback<Void, String> callback) {

        String path = String.format("recipes/%s", recipe.getRecipe_id());
        FirebaseDatabaseService.getInstance().delete(path, new GenericCallback<Void, String>() {
            @Override
            public void onSuccess(final Void value) {

                AppExecutors.getInstance().background().execute(new Runnable() {
                    @Override
                    public void run() {
                        RecipeDatabase.getInstance().getRecipeDao().deleteRecipe(recipe);

                        AppExecutors.getInstance().main().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(value);
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    public LiveData<Resource<Recipe>> searchRecipeBy(String id) {
        return mRecipeRepo.searchRecipeApi(id);
    }
}
