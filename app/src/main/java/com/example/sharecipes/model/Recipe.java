package com.example.sharecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {

    /* Properties */
    private String   recipe_id;
    private String   title;
    private String   publisher;
    private String[] ingredients;
    private float    social_rank;
    private String   image_url;

    /* Constructors */
    public Recipe() {}

    public Recipe(String recipe_id, String title, String publisher, String[] ingredients,
                  float social_rank, String image_url) {
        this.recipe_id = recipe_id;
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.social_rank = social_rank;
        this.image_url = image_url;
    }

    protected Recipe(Parcel in) {
        recipe_id = in.readString();
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArray();
        social_rank = in.readFloat();
        image_url = in.readString();
    }

    /* Getter and Setter */
    public String getRecipe_id() {
        return recipe_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImage_url() {
        return image_url;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

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
    }
}