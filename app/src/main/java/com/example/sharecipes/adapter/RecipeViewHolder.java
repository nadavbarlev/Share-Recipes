package com.example.sharecipes.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.sharecipes.R;
import com.example.sharecipes.model.Recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /* Views */
    private TextView textviewRecipeTitle;
    private TextView textViewRecipePublisher;
    private TextView textViewRecipeScore;
    private AppCompatImageView imageviewRecipe;

    /* Data Members */
    private RecipeCategoryViewHolderListener mListener;
    private RequestManager mGlideRequestManager;


    /* Constructor */
    public RecipeViewHolder(@NonNull View itemView,
                            RecipeCategoryViewHolderListener listener,
                            RequestManager requestManager) {
        super(itemView);

        /* Views */
        textviewRecipeTitle = itemView.findViewById(R.id.textview_recipe_title);
        textViewRecipePublisher = itemView.findViewById(R.id.textview_recipe_publisher);
        textViewRecipeScore = itemView.findViewById(R.id.textview_recipe_score);
        imageviewRecipe = itemView.findViewById(R.id.image_recipe);

        /* Members */
        this.mListener = listener;
        this.mGlideRequestManager = requestManager;

        /* Events */
        this.itemView.setOnClickListener(this);
    }

    /* Methods */
    public void onBind(Recipe recipe) {
        textviewRecipeTitle.setText(recipe.getTitle());
        textViewRecipePublisher.setText(recipe.getPublisher());
        textViewRecipeScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        mGlideRequestManager.load(recipe.getImage_url())
                            .into(imageviewRecipe);
    }

    /* Implement OnClickListener */
    @Override
    public void onClick(View v) {
        if (mListener == null) { return; }
        mListener.onRecipeClicked(getAdapterPosition());
    }


    public interface RecipeCategoryViewHolderListener {
        void onRecipeClicked(int position);
        void onCategoryClicked(String category);
    }
}
