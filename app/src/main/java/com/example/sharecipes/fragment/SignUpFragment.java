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
import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.firebase.callback.FirebaseAuthListener;
import com.example.sharecipes.util.ui.HorizontalDottedProgress;

import org.w3c.dom.Text;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    /* Views */
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button   buttonSignUp;
    private TextView textViewToSignIn;
    private HorizontalDottedProgress progressBar;

    /* Constructor */
    public SignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        /* Views */
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        textViewToSignIn = view.findViewById(R.id.textViewSignIn);
        progressBar = view.findViewById(R.id.progressBarSignUp);

        // Events
        buttonSignUp.setOnClickListener(this);
        textViewToSignIn.setOnClickListener(this);

        return view;
    }

    /* Private Methods */
    private void signUp(String email, String password) {

        // Check input validation
        if (!validate()) { return; }

        // Show ProgressBar
        showProgressBar(true);

        // Sign Out
        FirebaseAuthService.getInstance().signUp(email, password, new FirebaseAuthListener() {
            @Override
            public void onSuccess(String userID) {
                String path = String.format("users/%s", userID);
                String name = editTextName.getText().toString();
                FirebaseDatabaseService.getInstance().setValue(path, name);
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

        String name = editTextName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required.");
            isValid = false;
        } else {
            editTextName.setError(null);
        }

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

        if (viewID == R.id.buttonSignUp) {
            signUp(editTextEmail.getText().toString(),
                    editTextPassword.getText().toString());
        } else if (viewID == R.id.textViewSignIn) {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        }
    }
}
