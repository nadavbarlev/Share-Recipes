package com.example.sharecipes.firebase;

import com.example.sharecipes.firebase.callback.FirebaseDatabaseListener;
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
    public void setValue(String path, String data) {
        mRecipeRef.child(path).setValue(data);
    }

    public void setValue(String path, Map<String, String> data) {
        mRecipeRef.child(path).setValue(data);
    }

    public void getValue(String path, final FirebaseDatabaseListener listener) {

        // Read from the database
        mRecipeRef.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Map<String, String> mapRecipe = (Map<String, String>)dataSnapshot.getValue();
                listener.onSuccess(mapRecipe);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }
}
