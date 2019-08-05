package com.example.sharecipes.firebase;

import com.example.sharecipes.util.callback.Callback;
import com.example.sharecipes.util.callback.GenericCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class FirebaseAuthService {

    /* Data Members */
    private static FirebaseAuthService mInstance;

    private FirebaseAuth mAuth;

    /* Constructor */
    private FirebaseAuthService() {
        mAuth = FirebaseAuth.getInstance();
    }

    /* Singleton */
    public static FirebaseAuthService getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseAuthService();
        }
        return mInstance;
    }

    /* Methods */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signIn(String email, String password, final GenericCallback<String, String> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = task.getResult().getUser().getUid();
                            listener.onSuccess(userID);
                        } else {
                            String errorMsg = task.getException().getLocalizedMessage();
                            listener.onFailure(errorMsg);
                        }
                    }
                });
    }

    public void signUp(String email, String password, final GenericCallback<String, String> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = task.getResult().getUser().getUid();
                            listener.onSuccess(userID);
                        } else {
                            String errorMsg = task.getException().getLocalizedMessage();
                            listener.onFailure(errorMsg);
                        }
                    }
                });

    }

    public void SignOut() {
        mAuth.signOut();
    }

    public void updateNameAndEmail(final String newName, final String newEmail, String password, final Callback callback) {

        /* Update Email */
        AuthCredential credential = EmailAuthProvider.getCredential(getUserEmail(), password);
        getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getCurrentUser().updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            /* Update name */
                                            String path = String.format("users/%s", getUserID());
                                            FirebaseDatabaseService.getInstance().setValue(path, newName);

                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure();
                                        }
                                    }
                                });
                    }
                });

    }

    public String getUserID() {
        return mAuth.getCurrentUser().getUid();
    }

    public String getUserEmail() {
        return mAuth.getCurrentUser().getEmail();
    }
}
