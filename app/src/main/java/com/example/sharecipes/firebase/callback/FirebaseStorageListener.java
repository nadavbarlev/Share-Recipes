package com.example.sharecipes.firebase.callback;

import android.net.Uri;

public interface FirebaseStorageListener {
    void onSuccess(Uri uri);
    void onFailure();
}
