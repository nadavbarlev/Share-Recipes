package com.example.sharecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sharecipes.adapter.RecipeRecyclerAdapter;
import com.example.sharecipes.adapter.RecipeViewHolder;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.util.VerticalSpacingItemDecorator;
import com.example.sharecipes.viewmodel.RecipeListVM;
import com.example.sharecipes.viewmodel.ViewState;

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

        /* Setup Toolbar */
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        /*
        if (!mRecipeListVM.isRecipesDisplay()) {
            mAdapter.setCategories(mRecipeListVM.getCategories());
            mRecipeListVM.setIsRecipesDisplay(false);
        } */
    }

    /* Methods */
    private void setupViewModel() {
        mRecipeListVM = ViewModelProviders.of(this).get(RecipeListVM.class);

        // Observe to ViewState
        mRecipeListVM.getViewState().observe(this, new Observer<RecipeListVM.ViewState>() {
            @Override
            public void onChanged(@Nullable RecipeListVM.ViewState viewState) {
                if (viewState == null) { return; }
                switch (viewState) {
                    case RECIPES:
                        break;
                    case CATEGORIES:
                        List<Recipe> categories = mRecipeListVM.getCategories();
                        mAdapter.setCategories(categories);
                        break;
                }
            }
        });
/*
        // Observe to Recipes
        mRecipeListVM.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null) { return; }
                mAdapter.setRecipes(recipes);
                mRecipeListVM.setIsRecipesDisplay(true);
            }
        });

        // Observe to IsQueryExhausted
        mRecipeListVM.getIsQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                   mAdapter.setExhausted();
                }
            }
        });
        */
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new VerticalSpacingItemDecorator(30));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)) {
                    // mRecipeListVM.searchNextPage();
                }
            }
        });
    }

    private void setupSearchView() {
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.setProgress();
                mRecipeListVM.searchRecipe(s, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { return false; }
        });
    }

    /* Override Methods */
    @Override
    public void onBackPressed() {
        if (mRecipeListVM.isRecipesDisplay()) {
            mRecipeListVM.setIsRecipesDisplay(false);
            mAdapter.setCategories(mRecipeListVM.getCategories());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            mRecipeListVM.setIsRecipesDisplay(false);
            mAdapter.setCategories(mRecipeListVM.getCategories());
        }
        return super.onOptionsItemSelected(item);
    }

    /* Implement RecipeViewHolder.RecipeViewHolderListener */
    @Override
    public void onRecipeClicked(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClicked(String category) {
        mAdapter.setProgress();
        mRecipeListVM.searchRecipe(category, 1);
    }

}
