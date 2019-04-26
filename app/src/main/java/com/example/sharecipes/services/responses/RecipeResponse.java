package com.example.sharecipes.services.responses;

import com.example.sharecipes.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse {

    @SerializedName("recipe")
    @Expose
    private Recipe recipe;

    /* Getter */
    public Recipe getRecipe() {
        return recipe;
    }

    /* Override Methods */
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                '}';
    }
}
