package com.example.sharecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.viewmodel.RecipeListVM;

import java.util.List;

public class RecipeListActivity extends BaseActivity {

    /* Constants */
    private static final String TAG = "RecipeListActivity";

    /* Data Members */
    private RecipeListVM mRecipeListVM;

    /* LifeCycle */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListVM = ViewModelProviders.of(this).get(RecipeListVM.class);

        subscribeVM();

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchRecipe("milk", 1);

            }
        });
    }

    /* Methods */
    private void subscribeVM() {

        mRecipeListVM.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

                if (recipes == null) { return; }

                for(Recipe recipe: recipes) {
                    Log.d(TAG, "onChanged: " + recipe.getTitle());
                }

            }
        });

    }

    public void searchRecipe(String query, int page) {
        mRecipeListVM.searchRecipe(query, page);
    }
}
