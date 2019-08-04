package com.example.sharecipes.firebase;

import com.example.sharecipes.firebase.callback.FirebaseAuthListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

    public void signIn(String email, String password, final FirebaseAuthListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = task.getResult().getUser().getUid();
                            listener.onSuccess(userID);
                        } else {
                            String errorMsg = task.getException().getMessage();
                            listener.onFailure(errorMsg);
                        }
                    }
                });
    }

    public void signUp(String email, String password, final FirebaseAuthListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = task.getResult().getUser().getUid();
                            listener.onSuccess(userID);
                        } else {
                            String errorMsg = task.getException().getMessage();
                            listener.onFailure(errorMsg);
                        }
                    }
                });

    }

    public void SignOut() {
        mAuth.signOut();
    }
}
