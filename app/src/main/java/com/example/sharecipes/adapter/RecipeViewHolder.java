package com.example.sharecipes.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sharecipes.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /* Views */
    TextView textviewRecipeTitle;
    TextView textViewRecipePublisher;
    TextView textViewRecipeScore;
    AppCompatImageView imageviewRecipe;

    /* Data Members */
    RecipeViewHolderListener listener;

    /* Constructor */
    public RecipeViewHolder(@NonNull View itemView, RecipeViewHolderListener listener) {
        super(itemView);

        /* Views */
        textviewRecipeTitle = itemView.findViewById(R.id.textview_recipe_title);
        textViewRecipePublisher = itemView.findViewById(R.id.textview_recipe_publisher);
        textViewRecipeScore = itemView.findViewById(R.id.textview_recipe_score);
        imageviewRecipe = itemView.findViewById(R.id.image_recipe);

        this.listener = listener;
        this.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onRecipeClicked(getAdapterPosition());
    }


    public interface RecipeViewHolderListener {
        void onRecipeClicked(int position);
        void onCategoryClicked(String category);
    }
}
