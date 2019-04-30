package com.example.sharecipes.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sharecipes.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /* Data Members */
    private RecipeViewHolder.RecipeCategoryViewHolderListener listener;

    /* Views */
    CircleImageView imageViewCategory;
    TextView        textviewCategory;

    /* Constructor */
    public CategoryViewHolder(@NonNull View itemView, RecipeViewHolder.RecipeCategoryViewHolderListener listener) {
        super(itemView);

        /* Data Members */
        this.listener = listener;

        /* Views */
        imageViewCategory = itemView.findViewById(R.id.imageview_category);
        textviewCategory  = itemView.findViewById(R.id.textview_category);

        itemView.setOnClickListener(this);
    }

    /* Implement OnClickListener */
    @Override
    public void onClick(View v) {
        listener.onCategoryClicked(textviewCategory.getText().toString());
    }
}
