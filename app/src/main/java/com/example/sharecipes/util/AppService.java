package com.example.sharecipes.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.sharecipes.activity.ProfileActivity;
import com.example.sharecipes.activity.RecipeListActivity;
import com.example.sharecipes.activity.UploadActivity;

public class AppService extends Application {

    /* Data Members */
    private static Intent intentSearch;
    private static Intent intentProfile;
    private static Intent intentUpload;

    /* Constructor */
    private AppService() {}

    /* Methods */
    public static Intent getSearchIntent(Context context) {
        if (intentSearch == null) {
            intentSearch = new Intent(context, RecipeListActivity.class);
        }
        return intentSearch;
    }

    public static Intent getProfileIntent(Context context) {
        if (intentProfile == null) {
            intentProfile = new Intent(context, ProfileActivity.class);
        }
        return intentProfile;
    }

    public static Intent getUploadIntent(Context context) {
        if (intentUpload == null) {
            intentUpload = new Intent(context, UploadActivity.class);
        }
        return intentUpload;
    }
}
