package com.example.sharecipes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.sharecipes.R;
import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.util.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /* Constants */
    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    /* Data Members */
    public int adapterType = 1;
    private List<Recipe> mRecipes;
    private RecipeViewHolder.RecipeCategoryViewHolderListener mListener;
    private RequestManager mGlideRequestManager;

    /* Constructor */
    public RecipeRecyclerAdapter(RecipeViewHolder.RecipeCategoryViewHolderListener listener,
                                 RequestManager requestManager) {
        this.mListener = listener;
        this.mGlideRequestManager = requestManager;
    }

    /* Implement RecyclerView Adapter */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        switch (viewType) {
            case LOADING_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_exhausted_list_item, viewGroup, false);
                return new ExhaustedViewHolder(view);
            case CATEGORY_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, mListener, mGlideRequestManager);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate (R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, mListener, mGlideRequestManager);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        /* Recipes Section */
        if (getItemViewType(i) == RECIPE_TYPE) {
            final Recipe currRecipe = mRecipes.get(i);
            ((RecipeViewHolder)viewHolder).onBind(currRecipe);
        }

        /* Category Section */
        else if (getItemViewType(i) == CATEGORY_TYPE) {
            final Recipe currRecipe = mRecipes.get(i);
            ((CategoryViewHolder)viewHolder).onBind(currRecipe);
        }
    }

    @Override
    public int getItemCount() {
        switch (adapterType) {
            case RECIPE_TYPE:
                return mRecipes.size();
            case LOADING_TYPE:
                return 1;
            case EXHAUSTED_TYPE:
                return mRecipes.size() + 1; // TODO: Check
            case CATEGORY_TYPE:
                return Constants.DEFAULT_SEARCH_CATEGORIES.length;
            default:
                return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (adapterType == EXHAUSTED_TYPE && position != mRecipes.size()) {
            return RECIPE_TYPE;
        }
        return adapterType;
    }

    /* Methods */
    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        setAdapterType(RECIPE_TYPE);
    }

    public void setCategories(List<Recipe> recipes) {
        this.mRecipes = recipes;
        setAdapterType(CATEGORY_TYPE);
    }

    public void setProgress() {
        if (adapterType != LOADING_TYPE) {
            setAdapterType(LOADING_TYPE);
        }
    }

    public void setExhausted() {
        setAdapterType(EXHAUSTED_TYPE);
    }

    public Recipe getSelectedRecipe(int position) {
        if (mRecipes == null ||
            mRecipes.size() < 1 ||
            adapterType != RECIPE_TYPE) { return null; }
        return mRecipes.get(position);
    }

    /* Private Methods */
    private void setAdapterType(int type) {
        adapterType = type;
        notifyDataSetChanged();
    }
}
