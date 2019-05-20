package com.example.sharecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.viewmodel.RecipeVM;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    /* Data Members */
    private RecipeVM mRecipeVM;

    /* Views */
    private ScrollView mScrollViewContainer;
    private AppCompatImageView mImageViewRecipe;
    private TextView mTextViewTitle;
    private TextView mTextViewScore;
    private LinearLayout mLinearLayoutIngredientsContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        /* Views */
        mScrollViewContainer = findViewById(R.id.scrollViewRecipeDetails);
        mImageViewRecipe = findViewById(R.id.imageView_recipe);
        mTextViewTitle = findViewById(R.id.textView_recipe_title);
        mTextViewScore = findViewById(R.id.textView_recipe_score);
        mLinearLayoutIngredientsContainer = findViewById(R.id.linearLayout_ingredients_container);

        setupViewModel();
        getIncomingIntent();
    }

    /* Private Methods */
    private void setupViewModel() {
        mRecipeVM = ViewModelProviders.of(this).get(RecipeVM.class);
        mRecipeVM.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                Log.d(TAG, recipe.getIngredients()[0]);
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, recipe.getTitle());
            mRecipeVM.searchRecipeBy(recipe.getRecipe_id());
        }
    }
}
