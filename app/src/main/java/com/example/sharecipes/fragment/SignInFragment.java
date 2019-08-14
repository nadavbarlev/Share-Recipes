package com.example.sharecipes.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharecipes.R;
import com.example.sharecipes.activity.RecipeListActivity;
import com.example.sharecipes.firebase.FirebaseAuthService;
import com.example.sharecipes.util.callback.GenericCallback;
import com.example.sharecipes.util.ui.HorizontalDottedProgress;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignInFragment";

    /* Views */
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewToSignUp;
    private Button buttonSignIn;
    private HorizontalDottedProgress progressBar;

    /* Constructor */
    public SignInFragment() {}

    /* LifeCycle */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        /* Views */
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSignIn = view.findViewById(R.id.buttonSignIn);
        textViewToSignUp = view.findViewById(R.id.textViewSignUp);
        progressBar = view.findViewById(R.id.progressBarSignIn);

        // Events
        buttonSignIn.setOnClickListener(this);
        textViewToSignUp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in
        FirebaseUser currentUser =
                FirebaseAuthService.getInstance().getCurrentUser();
        if (currentUser != null) {
            moveToApp();
        }
    }


    /* Private Methods */
    private void signIn(String email, String password) {

        // Check input validation
        if (!validate()) { return; }

        // Show ProgressBar
       showProgressBar(true);

        // Sign In
        FirebaseAuthService.getInstance().signIn(email, password, new GenericCallback<String, String>() {
            @Override
            public void onSuccess(String userID) {
                showProgressBar(false);
                moveToApp();
            }

            @Override
            public void onFailure(String errorMsg) {
                showProgressBar(false);
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        String email = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            isValid = false;
        } else {
            editTextEmail.setError(null);
        }

        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            isValid = false;
        } else {
            editTextPassword.setError(null);
        }

        return isValid;
    }

    private void moveToApp() {
        Intent intent = new Intent(getContext(), RecipeListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void showProgressBar(Boolean visibility) {
        progressBar.clearAnimation();
        progressBar.setVisibility( visibility ? View.VISIBLE : View.INVISIBLE);
    }

    /* Implement View.OnClickListener */
    @Override
    public void onClick(View view) {

        int viewID = view.getId();

        if (viewID == R.id.buttonSignIn) {
            signIn(editTextEmail.getText().toString(),
                    editTextPassword.getText().toString());
        } else if (viewID == R.id.textViewSignUp) {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.signUpFragment);
        }
    }
}
