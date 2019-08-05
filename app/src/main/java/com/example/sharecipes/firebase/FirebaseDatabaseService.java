package com.example.sharecipes.firebase;

import com.example.sharecipes.util.callback.GenericCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class FirebaseDatabaseService {

    /* Data Members */
    private static FirebaseDatabaseService mInstance;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRecipeRef;

    /* Constructor */
    private FirebaseDatabaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mRecipeRef = mDatabase.getReference("recipes");
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
        mRecipeRef.child(path).setValue(data);
    }

    public void setValue(String path, Map<String, String> data) {
        mRecipeRef.child(path).setValue(data);
    }

    /* Generic Getters */
    public void getValue(String path, final GenericCallback<String, String> listener) {

        mRecipeRef.child(path).addValueEventListener(new ValueEventListener() {
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
}
