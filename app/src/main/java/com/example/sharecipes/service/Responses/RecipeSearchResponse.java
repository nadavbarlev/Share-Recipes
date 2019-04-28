package com.example.sharecipes.service.Responses;

import com.example.sharecipes.model.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

public class RecipeSearchResponse {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes;

    /* Getter */
    public int getCount() {
        return count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    /* Override Methods */
    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                '}';
    }
}
