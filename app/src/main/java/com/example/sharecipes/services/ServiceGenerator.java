package com.example.sharecipes.services;

import com.example.sharecipes.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    /* Retrofit */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();

    /* Recipe Service */
    private static IRecipeService recipeService = retrofit.create(IRecipeService.class);
    public static IRecipeService getRecipeService() {
        return recipeService;
    }

}
