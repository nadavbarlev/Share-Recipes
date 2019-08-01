package com.example.sharecipes.service;

import com.example.sharecipes.service.Responses.RecipeResponse;
import com.example.sharecipes.service.Responses.RecipeSearchResponse;
import com.example.sharecipes.util.network.ApiResponse;

import androidx.lifecycle.LiveData;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRecipeService {

    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );

    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );
}
