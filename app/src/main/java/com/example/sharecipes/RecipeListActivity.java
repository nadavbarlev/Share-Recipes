package com.example.sharecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sharecipes.adapter.RecipeRecyclerAdapter;
import com.example.sharecipes.adapter.RecipeViewHolder;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.viewmodel.RecipeListVM;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements RecipeViewHolder.RecipeViewHolderListener {

    /* Constants */
    private static final String TAG = "RecipeListActivity";

    /* Data Members */
    private RecipeListVM mRecipeListVM;
    private RecipeRecyclerAdapter mAdapter;

    /* Views */
    private RecyclerView mRecyclerView;

    /* LifeCycle */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        /* RecyclerView Init */
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        /* ViewModel */
        mRecipeListVM = ViewModelProviders.of(this).get(RecipeListVM.class);

        subscribeVM();

        searchRecipe("milk", 1);
    }

    /* Methods */
    private void subscribeVM() {

        mRecipeListVM.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

                if (recipes == null) { return; }
                mAdapter.setRecipes(recipes);
            }
        });
    }

    public void searchRecipe(String query, int page) {
        mRecipeListVM.searchRecipe(query, page);
    }

    @Override
    public void onRecipeClicked(int position) {

    }

    @Override
    public void onCategoryClicked(String category) {

    }
}
