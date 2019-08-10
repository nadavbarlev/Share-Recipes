package com.example.sharecipes.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sharecipes.R;
import com.example.sharecipes.firebase.FirebaseAuthService;
import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.util.callback.GenericCallback;
import com.example.sharecipes.util.network.Resource;
import com.example.sharecipes.viewmodel.RecipeVM;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    /* Data Members */
    private RecipeVM mRecipeVM;
    private Recipe mRecipe;

    /* Views */
    private ScrollView mScrollViewContainer;
    private AppCompatImageView mImageViewRecipe;
    private TextView mTextViewTitle;
    private TextView mTextViewScore;
    private LinearLayout mLinearLayoutIngredientsContainer;
    private Button mButtonDelete;

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
        mButtonDelete = findViewById(R.id.buttonDelete);

        showProgressBar(true);

        setupViewModel();
        getIncomingIntent();
        setupButtonDelete();
    }

    /* Private Methods */
    private void setupViewModel() {
        mRecipeVM = ViewModelProviders.of(this).get(RecipeVM.class);
    }

    private void setupButtonDelete() {

        // Button Visibility
        String currUserID = FirebaseAuthService.getInstance().getUserID();
        if (mRecipe.getUser_id() == null || !mRecipe.getUser_id().equals(currUserID)) {
            mButtonDelete.setVisibility(View.GONE);
        }

        // Button Events
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecipeVM.deleteRecipe(mRecipe, new GenericCallback<Void, String>() {
                    @Override
                    public void onSuccess(Void value) {
                        Toast.makeText(RecipeActivity.this,
                                "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(RecipeActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
            mRecipe = getIntent().getParcelableExtra("recipe");
            mRecipeVM.searchRecipeBy(mRecipe.getRecipe_id()).observe(this, new Observer<Resource<Recipe>>() {
                @Override
                public void onChanged(Resource<Recipe> recipeResource) {
                    if (recipeResource == null || recipeResource.data == null) { return; }
                    switch (recipeResource.status) {
                        case LOADING:
                            showProgressBar(true);
                            break;
                        case ERROR:
                            if (recipeResource.data != null) {
                                setViewsProperties(recipeResource.data);
                            } else {
                                showErrorScreen(recipeResource.message);
                            }
                            break;
                        case SUCCESS:
                            setViewsProperties(recipeResource.data);
                            break;
                    }
                }
            });
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
