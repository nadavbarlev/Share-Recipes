package com.example.sharecipes.presistence;

import android.content.Context;

import com.example.sharecipes.model.Recipe;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters({RecipeConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    // Constants
    private static final String DATABASE_NAME = "db_recipes";

    // Constructor
    public RecipeDatabase() {}

    // Singleton
    private static RecipeDatabase instance = null;
    public static RecipeDatabase getInstance(Context context) {
        if (instance != null) { return instance; }
        instance = Room.databaseBuilder(context.getApplicationContext(),
                                        RecipeDatabase.class,
                                        DATABASE_NAME).build();
        return instance;
    }

    // Methods
    public abstract RecipeDao getRecipeDao();
}
