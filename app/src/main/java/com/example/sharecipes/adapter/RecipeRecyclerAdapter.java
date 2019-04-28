package com.example.sharecipes.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sharecipes.R;
import com.example.sharecipes.model.Recipe;

import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /* Data Members */
    private List<Recipe> mRecipes;
    private RecipeViewHolder.RecipeViewHolderListener mListener;

    /* Constructor */
    public RecipeRecyclerAdapter(RecipeViewHolder.RecipeViewHolderListener listener) {
        this.mListener = listener;
    }

    /* Implement RecyclerView Adapter */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate (R.layout.layout_recipe_list_item, viewGroup, false);
        return new RecipeViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final Recipe currRecipe = mRecipes.get(i);
        ((RecipeViewHolder)viewHolder).textviewRecipeTitle.setText(currRecipe.getTitle());
        ((RecipeViewHolder)viewHolder).textViewRecipePublisher.setText(currRecipe.getPublisher());
        ((RecipeViewHolder)viewHolder).textViewRecipeScore.setText(String.valueOf(Math.round(currRecipe.getSocial_rank())));

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(currRecipe.getImage_url())
                .into(((RecipeViewHolder)viewHolder).imageviewRecipe);
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) { return 0; }
        return mRecipes.size();
    }

    /* Methods */
    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }
}
