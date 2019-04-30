package com.example.sharecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import com.example.sharecipes.adapter.RecipeRecyclerAdapter;
import com.example.sharecipes.adapter.RecipeViewHolder;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.viewmodel.RecipeListVM;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements RecipeViewHolder.RecipeCategoryViewHolderListener {

    /* Constants */
    private static final String TAG = "RecipeListActivity";

    /* Data Members */
    private RecipeListVM mRecipeListVM;
    private RecipeRecyclerAdapter mAdapter;

    /* Views */
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;

    /* LifeCycle */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        /* Setup Components */
        setupSearchView();
        setupRecyclerView();

        /* Setup Members */
        setupViewModel();

        if (!mRecipeListVM.isRecipesDisplay()) {
            mAdapter.setCategories(mRecipeListVM.getCategories());
        }
    }

    /* Methods */
    private void setupViewModel() {
        mRecipeListVM = ViewModelProviders.of(this).get(RecipeListVM.class);
        mRecipeListVM.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null) { return; }
                mAdapter.setRecipes(recipes);
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupSearchView() {
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.displayProgress();
                mRecipeListVM.searchRecipe(s, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { return false; }
        });
    }


    /* Implement RecipeViewHolder.RecipeViewHolderListener */
    @Override
    public void onRecipeClicked(int position) {

    }

    @Override
    public void onCategoryClicked(String category) {
        mAdapter.displayProgress();
        mRecipeListVM.searchRecipe(category, 1);
    }
}
