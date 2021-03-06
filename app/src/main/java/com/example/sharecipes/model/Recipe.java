package com.example.sharecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    /* Properties */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipe_id")
    private String recipe_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "ingredients")
    private String[] ingredients;

    @ColumnInfo(name = "social_rank")
    private float social_rank;

    @ColumnInfo(name = "image_url")
    private String image_url;

    @ColumnInfo(name = "timestamp")
    private int timestamp;

    @ColumnInfo(name = "user_id")
    private String user_id;

    /* Constructors */
    public Recipe() {}

    public Recipe(String recipe_id, String title, String publisher, String[] ingredients,
                  float social_rank, String image_url, int timestamp, String user_id) {
        this.recipe_id = recipe_id;
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.social_rank = social_rank;
        this.image_url = image_url;
        this.timestamp = timestamp;
        this.user_id = user_id;
    }

    protected Recipe(Parcel in) {
        recipe_id = in.readString();
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArray();
        social_rank = in.readFloat();
        image_url = in.readString();
        timestamp = in.readInt();
        user_id = in.readString();
    }

    /* Getter and Setter */
    public String getRecipe_id() { return recipe_id; }

    public String getTitle() { return title; }

    public String getPublisher() { return publisher; }

    public String getImage_url() { return image_url; }

    public String[] getIngredients() {  return ingredients; }

    public float getSocial_rank() { return social_rank; }

    public int getTimestamp() { return timestamp; }

    public String getUser_id() { return user_id; }

    public void setRecipe_id(String recipe_id) { this.recipe_id = recipe_id; }

    public void setTitle(String title) { this.title = title; }

    public void setPublisher(String publisher) { this.publisher = publisher; }

    public void setIngredients(String[] ingredients) { this.ingredients = ingredients; }

    public void setSocial_rank(float social_rank) { this.social_rank = social_rank; }

    public void setImage_url(String image_url) {  this.image_url = image_url; }

    public void setTimestamp(int timestamp) {  this.timestamp = timestamp; }

    public void setUser_id(String user_id) {  this.user_id = user_id; }

    /* Override Methods */
    @Override
    public String toString() {
        return "Recipe{" +
                "recipe_id='" + recipe_id + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", social_rank=" + social_rank +
                ", image_url='" + image_url + '\'' +
                ", timestamp=" + timestamp +
                ", timestamp=" + user_id +
                '}';
    }

    /* Parcelable - Creator */
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /* Parcelable - Implementation */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipe_id);
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeStringArray(ingredients);
        dest.writeFloat(social_rank);
        dest.writeString(image_url);
        dest.writeInt(timestamp);
        dest.writeString(user_id);
    }

    /* Converter */
    public static Recipe toRecipe(Map<String, String> mapRecipe) {
        String title = mapRecipe.get("title");
        String publisher = mapRecipe.get("publisher");
        String[] ingredients = mapRecipe.get("ingredients").split("\n");
        String image_url = mapRecipe.get("uri");
        String user_id = mapRecipe.get("user_id");
        String recipeID = mapRecipe.get("key");

        return new Recipe(recipeID, title, publisher, ingredients, 100, image_url, 0, user_id);
    }
}