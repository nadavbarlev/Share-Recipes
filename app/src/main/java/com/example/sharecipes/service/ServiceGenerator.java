package com.example.sharecipes.service;

import com.example.sharecipes.util.Constants;
import com.example.sharecipes.util.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    /* Retrofit */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();

    /* Recipe Service */
    private static IRecipeService recipeService = retrofit.create(IRecipeService.class);
    public static IRecipeService getRecipeService() {
        return recipeService;
    }

    /* Another Service */
}
