package com.example.sharecipes.firebase;

import android.content.Context;

import com.example.sharecipes.model.Recipe;
import com.example.sharecipes.presistence.RecipeDao;
import com.example.sharecipes.presistence.RecipeDatabase;
import com.example.sharecipes.util.AppExecutors;
import com.example.sharecipes.util.callback.Callback;
import com.example.sharecipes.util.callback.GenericCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class FirebaseDatabaseService {

    /* Data Members */
    private static FirebaseDatabaseService mInstance;
    private FirebaseDatabase mDatabase;

    /* Constructor */
    private FirebaseDatabaseService() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    /* Singleton */
    public static FirebaseDatabaseService getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseDatabaseService();
        }
        return mInstance;
    }

    /* Methods */
    public void getUsername(final GenericCallback<String, String> callback) {

        String path = String.format("users/%s", FirebaseAuthService.getInstance().getUserID());

        FirebaseDatabaseService.getInstance().getValue(path, new GenericCallback<String, String>() {
            @Override
            public void onSuccess(String value) {
                callback.onSuccess(value);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    /* Generic Setters */
    public void setValue(String path, String data) {
        mDatabase.getReference().child(path).setValue(data);
    }

    public void setValue(String path, Map<String, String> data) {
        mDatabase.getReference().child(path).setValue(data);
    }

    /* Generic Getters */
    public void getValue(String path, final GenericCallback<String, String> listener) {

        mDatabase.getReference().child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onFailure(error.getMessage());
            }
        });
    }

    public void getValues(String path, final GenericCallback<Map<String, String>, String> listener) {

        mDatabase.getReference().child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> values = (Map<String, String>)dataSnapshot.getValue();
                listener.onSuccess(values);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onFailure(error.getMessage());
            }
        });
    }

    /* Search and Contains */
    public void contains(String path, String field, String text, final GenericCallback<Map<String, String>, String> listener) {

        mDatabase.getReference().child(path).orderByChild(field).startAt(text).endAt(text+ "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Map<String, String> values = (Map<String, String>)child.getValue();
                            values.put("key", child.getKey());
                            listener.onSuccess(values);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onFailure(databaseError.getMessage());
                    }
                });
    }

    /* Update */
    public void update(final String path, final String whereField, final String whereValue,
                       final String toChangeField , final String toChangeValue, final Callback callback) {

       mDatabase.getReference().child(path).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // Gets Keys and Value
               Map<String, Map<String, String>> values = (Map<String, Map<String, String>>)dataSnapshot.getValue();

               // Iterate on all keys
               for (String key: values.keySet()) {

                   // Get values
                   Map<String, String> value = values.get(key);

                   // WHERE statement
                   if (value.get(whereField).equals(whereValue)) {

                       // Update Firebase
                       mDatabase.getReference().child(path).child(key).child(toChangeField).setValue(toChangeValue);

                       // Update DataBase
                       value.put(toChangeField, toChangeValue);
                       final Recipe recipe = Recipe.toRecipe(value);

                       AppExecutors.getInstance().background().execute(new Runnable() {
                           @Override
                           public void run() {
                               RecipeDatabase.getInstance().getRecipeDao().updateRecipe(recipe.getRecipe_id(),
                                       recipe.getTitle(), recipe.getPublisher(), recipe.getSocial_rank(), recipe.getImage_url());
                           }
                       });
                   }

               }

               callback.onSuccess();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               callback.onFailure();
           }
       });
    }

    /* Delete */
    public void delete(String path, final GenericCallback<Void, String> callback) {
        mDatabase.getReference().child(path).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onSuccess(null);
                    return;
                }
                callback.onFailure(databaseError.getMessage());
            }
        });
    }
}
