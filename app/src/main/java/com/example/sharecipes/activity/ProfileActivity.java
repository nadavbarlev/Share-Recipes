package com.example.sharecipes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharecipes.R;
import com.example.sharecipes.firebase.FirebaseAuthService;
import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.util.callback.Callback;
import com.example.sharecipes.util.callback.GenericCallback;
import com.example.sharecipes.util.ui.HorizontalDottedProgress;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    /* Views */
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSave;
    private Button buttonSignOut;
    private HorizontalDottedProgress progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* Views */
        editTextName = findViewById(R.id.editTextProfileName);
        editTextEmail = findViewById(R.id.editTextProfileEmail);
        editTextPassword = findViewById(R.id.editTextProfilePassword);
        buttonSave = findViewById(R.id.buttonProfileSave);
        buttonSignOut = findViewById(R.id.buttonSignOut);
        progressBar = findViewById(R.id.progressBarProfile);

        /* Events */
        buttonSave.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);

        setupViews();
    }

    /* Private Methods */
    private void setupViews() {

        // Get Username
        FirebaseDatabaseService.getInstance().getUsername(new GenericCallback<String, String>() {
            @Override
            public void onSuccess(String value) {

                // Set views
                editTextEmail.setText(FirebaseAuthService.getInstance().getUserEmail());
                editTextName.setText(value);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ProfileActivity.this, "Failed to load your data", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void save(String name, String email) {

        if (!validate()) { return; }

        String password = editTextPassword.getText().toString();

        FirebaseAuthService.getInstance().updateNameAndEmail(name, email, password, new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ProfileActivity.this, "Update Succeeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {

        // Sign Out
        FirebaseAuthService.getInstance().SignOut();

        // Move to Login screen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
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

    /* Implement View.OnClickListener */
    @Override
    public void onClick(View view) {

        int viewID = view.getId();

        if (viewID == R.id.buttonProfileSave) {
            save(editTextName.getText().toString(),
                 editTextEmail.getText().toString());
        } else if (viewID == R.id.buttonSignOut) {
           signOut();
        }
    }
}
