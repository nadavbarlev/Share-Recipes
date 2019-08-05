package com.example.sharecipes.firebase;

import android.net.Uri;

import com.example.sharecipes.util.callback.GenericCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;

public class FirebaseStorageService {

    /* Data Members */
    private static FirebaseStorageService mInstance;
    private StorageReference mStorageRef;

    /* Constructor */
    private FirebaseStorageService() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    /* Singleton */
    public static FirebaseStorageService getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseStorageService();
        }
        return mInstance;
    }

    /* Methods */
    public void upload(String path, Uri uri, final GenericCallback<Uri, String> listener) {

        mStorageRef.child(path).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                        listener.onSuccess(downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        listener.onFailure(exception.getLocalizedMessage());
                    }
                });
    }
}
