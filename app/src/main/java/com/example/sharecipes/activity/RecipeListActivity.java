package com.example.sharecipes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.sharecipes.R;
import com.example.sharecipes.adapter.RecipeRecyclerAdapter;
import com.example.sharecipes.adapter.RecipeViewHolder;
import com.example.sharecipes.firebase.FirebaseAuthService;
import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.firebase.callback.FirebaseDatabaseListener;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.util.network.Resource;
import com.example.sharecipes.util.ui.VerticalSpacingItemDecorator;
import com.example.sharecipes.viewmodel.RecipeListVM;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                        /* Recipes will show automatically from other observer */
                        break;
                    case CATEGORIES:
                        List<Recipe> categories = mRecipeListVM.getCategories();
                        mAdapter.setCategories(categories);
                        break;
                }
            }
        });

        // Observe to Recipes
        mRecipeListVM.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(final Resource<List<Recipe>> listResource) {
                if (listResource == null || listResource.data == null) { return; }
                switch (listResource.status) {
                    case LOADING:
                        mAdapter.setProgress();
                        break;
                    case ERROR:
                        mAdapter.setRecipes(listResource.data);
                        Toast.makeText(RecipeListActivity.this, listResource.message,Toast.LENGTH_SHORT).show();
                        if (listResource.message.equals(RecipeListVM.QUERY_EXHAUSTED)) {
                            mAdapter.setExhausted();
                        }
                        break;
                    case SUCCESS:

                        FirebaseDatabaseService.getInstance().getValue("nadav", new FirebaseDatabaseListener() {
                            @Override
                            public void onSuccess(Map<String, String> mapRecipe) {
                               Recipe recipe = Recipe.toRecipe(mapRecipe);
                               listResource.data.add(0, recipe);
                               mAdapter.setRecipes(listResource.data);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });

                        //mAdapter.setRecipes(listResource.data);
                        break;
                }
            }
        });
    }


    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new VerticalSpacingItemDecorator(30));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (mRecipeListVM.getViewState().getValue() == RecipeListVM.ViewState.RECIPES &&
                    !mRecyclerView.canScrollVertically(1)) {
                    mRecipeListVM.searchNextPage();
                }
            }
        });

        mAdapter = new RecipeRecyclerAdapter(this, setupGlide());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupSearchView() {
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mRecyclerView.smoothScrollToPosition(0);
                mRecipeListVM.searchRecipe(s, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { return false; }
        });
    }

    private RequestManager setupGlide() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);
        return Glide.with(this)
                .setDefaultRequestOptions(requestOptions);
    }

    /* Override Methods - Back Button */
    @Override
    public void onBackPressed() {
        if (mRecipeListVM.getViewState().getValue() == RecipeListVM.ViewState.RECIPES) {
            mRecipeListVM.setViewState(RecipeListVM.ViewState.CATEGORIES);
        } else {
            super.onBackPressed();
        }
    }

    /* Override Methods - Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            FirebaseAuthService.getInstance().SignOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } else if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AddRecipeActivity.class);
            startActivity(intent);
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
        mRecipeListVM.searchRecipe(category, 1);
    }
}
