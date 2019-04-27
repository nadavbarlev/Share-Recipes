package com.example.sharecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
    }

    /* Methods */
    private void subscribeVM() {

        mRecipeListVM.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

            }
        });

    }
}
