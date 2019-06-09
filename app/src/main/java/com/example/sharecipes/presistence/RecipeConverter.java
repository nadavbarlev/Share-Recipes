package com.example.sharecipes.presistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import androidx.room.TypeConverter;

public class RecipeConverter {

    @TypeConverter
    public static String[] from(String value) {
        Type type = new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String from(String[] value) {
        return new Gson().toJson(value);
    }
}
