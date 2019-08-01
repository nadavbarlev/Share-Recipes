package com.example.sharecipes.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.sharecipes.R;
import com.example.sharecipes.model.Recipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /* Data Members */
    private RecipeViewHolder.RecipeCategoryViewHolderListener mListener;
    private RequestManager mGlideRequestManager;

    /* Views */
    CircleImageView imageViewCategory;
    TextView        textviewCategory;

    /* Constructor */
    public CategoryViewHolder(@NonNull View itemView,
                              RecipeViewHolder.RecipeCategoryViewHolderListener listener,
                              RequestManager requestManager) {
        super(itemView);

        /* Views */
        imageViewCategory = itemView.findViewById(R.id.imageview_category);
        textviewCategory  = itemView.findViewById(R.id.textview_category);

        /* Data Members */
        this.mGlideRequestManager = requestManager;
        this.mListener = listener;

        /* Events */
        itemView.setOnClickListener(this);
    }

    /* Methods */
    public void onBind(Recipe recipe) {
        textviewCategory.setText(recipe.getTitle());
        mGlideRequestManager.load(Uri.parse(recipe.getImage_url()))
                            .into(imageViewCategory);
    }

    /* Implement OnClickListener */
    @Override
    public void onClick(View v) {
        if (mListener == null) { return; }
        mListener.onCategoryClicked(textviewCategory.getText().toString());
    }
}
