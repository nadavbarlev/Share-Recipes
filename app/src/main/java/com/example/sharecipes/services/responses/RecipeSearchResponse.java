package com.example.sharecipes.services.responses;

import com.example.sharecipes.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;

public class RecipeSearchResponse {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("recipes")
    @Expose
    private Recipe[] recipes;

    /* Getter */
    public int getCount() {
        return count;
    }

    public Recipe[] getRecipes() {
        return recipes;
    }

    /* Override Methods */
    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + Arrays.toString(recipes) +
                '}';
    }
}
