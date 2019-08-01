package com.example.sharecipes.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sharecipes.R;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.viewmodel.RecipeVM;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProviders;

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

        showProgressBar(true);

        setupViewModel();
        getIncomingIntent();
    }

    /* Private Methods */
    private void setupViewModel() {
        mRecipeVM = ViewModelProviders.of(this).get(RecipeVM.class);

        // Observe to Recipe
        /*
        mRecipeVM.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe == null ||
                    !recipe.getRecipe_id().equals(mRecipeVM.getRecipeID())) { return; }
                mRecipeVM.setIsRetrieveRecipe(true);
                setViewsProperties(recipe);
            }
        });

        // Observe to Network Timeout
        mRecipeVM.getIsNetworkTimeout().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean && !mRecipeVM.getIsRetrieveRecipe()) {
                    showErrorScreen("Error Retrieving Data. Please check network connection");
                }
            }
        });
        */
    }

    private void setViewsProperties(Recipe recipe) {

        // Check if post null
        if (recipe == null) { return; }

        // Set Text Parameters
        mTextViewTitle.setText(recipe.getTitle());
        mTextViewScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        // Load Recipe image
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(recipe.getImage_url())
                .into(mImageViewRecipe);

        // Each Ingredients is TextView
        for (String ingredient: recipe.getIngredients()) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                   ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(15);
            textView.setText(ingredient);
            mLinearLayoutIngredientsContainer.addView(textView);
        }

        PropertiesDidLoad();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            //mRecipeVM.searchRecipeBy(recipe.getRecipe_id());
        }
    }

    private void showErrorScreen(String message) {
       mTextViewTitle.setText("Error Retrieving Recipe");
       mTextViewScore.setText("");
       Glide.with(this)
               .load(R.drawable.ic_launcher_background)
               .into(mImageViewRecipe);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(15);
        textView.setText(message);
        mLinearLayoutIngredientsContainer.addView(textView);

        PropertiesDidLoad();
    }

    private void PropertiesDidLoad() {
        mScrollViewContainer.setVisibility(View.VISIBLE);
        showProgressBar(false);
    }
}
