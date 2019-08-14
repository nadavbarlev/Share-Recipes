package com.example.sharecipes.presistence;

import com.example.sharecipes.model.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    @Insert(onConflict = IGNORE)
    long[] insertRecipes(Recipe... recipes);

    @Insert(onConflict = REPLACE)
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("Delete from recipes")
    void deleteAll();

    @Query("UPDATE recipes SET title = :title, publisher = :publisher, image_url = :image_url, " +
            "social_rank = :social_rank WHERE recipe_id = :recipe_id")
    void updateRecipe(String recipe_id, String title, String publisher, float social_rank, String image_url);

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' OR ingredients LIKE '%' || :query || '%' " +
            "ORDER BY social_rank DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Recipe>> searchRecipe(String query, int pageNumber);

    @Query("SELECT * FROM recipes WHERE recipe_id = :recipe_id")
    LiveData<Recipe> getRecipe(String recipe_id);
}
