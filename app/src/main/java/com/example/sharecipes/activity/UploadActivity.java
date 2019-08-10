package com.example.sharecipes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharecipes.R;
import com.example.sharecipes.firebase.FirebaseAuthService;
import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.firebase.FirebaseStorageService;
import com.example.sharecipes.util.AppService;
import com.example.sharecipes.util.callback.GenericCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadActivity extends BaseActivity implements View.OnClickListener {

    // Data Members
    private final int GALLERY_REQUEST_CODE = 1;
    private Uri uriImage = null;

    // Views
    private AppCompatImageView imageViewRecipe;
    private EditText editTextTitle;
    private EditText editTextIngredients;
    private Button buttonUpload;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Views
        imageViewRecipe = findViewById(R.id.imageView_recipe);
        editTextTitle = findViewById(R.id.editText_recipe_title);
        editTextIngredients = findViewById(R.id.editText_recipe_ingredients);
        buttonUpload = findViewById(R.id.buttonUploadRecipe);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewUpload);

        // Events
        imageViewRecipe.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        setupBottomNavigationView();
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    uriImage = data.getData();
                    imageViewRecipe.setImageURI(uriImage);
                    break;
            }
    }

    /* Private Methods */
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    private void uploadRecipe() {

        // Validation
        if (!validate()) { return; }

        showProgressBar(true);

        final String title = editTextTitle.getText().toString();
        final String ingredients = editTextIngredients.getText().toString();

        // Gets Username
        FirebaseDatabaseService.getInstance().getUsername(new GenericCallback<String, String>() {
            @Override
            public void onSuccess(final String publisher) {

                // Get RANDOM string as Recipe id
                final String recipeID = UUID.randomUUID().toString();

                // Upload image recipe to storage
                FirebaseStorageService.getInstance().upload(recipeID, uriImage, new GenericCallback<Uri, String>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // Get recipe details
                        Map<String, String> dicRecipe = new HashMap<String, String>();

                        dicRecipe.put("user_id", FirebaseAuthService.getInstance().getUserID());
                        dicRecipe.put("title", title);
                        dicRecipe.put("publisher", publisher);
                        dicRecipe.put("ingredients", ingredients);
                        dicRecipe.put("uri", uri.toString());

                        // Upload recipe to DB
                        String path = String.format("recipes/%s", recipeID);
                        FirebaseDatabaseService.getInstance().setValue(path, dicRecipe);

                        showProgressBar(false);
                        Toast.makeText(UploadActivity.this,  "Recipe Uploaded successfully", Toast.LENGTH_SHORT).show();
                        clearViews();
                    }

                    @Override
                    public void onFailure(String error) {
                        showProgressBar(false);
                        Toast.makeText(UploadActivity.this,  error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                showProgressBar(false);
                Toast.makeText(UploadActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        if (uriImage == null) {
            Toast.makeText(this, "Image is missing", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        String email = editTextTitle.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextTitle.setError("Required.");
            isValid = false;
        } else {
            editTextTitle.setError(null);
        }

        String password = editTextIngredients.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextIngredients.setError("Required.");
            isValid = false;
        } else {
            editTextIngredients.setError(null);
        }

        return isValid;
    }

    private void clearViews() {
        editTextTitle.setText(null);
        editTextIngredients.setText(null);
        imageViewRecipe.setImageResource(R.drawable.camera);
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationViewUpload);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemSearch:
                        Intent intentSearch = AppService.getSearchIntent(UploadActivity.this);
                        startActivity(intentSearch);
                        UploadActivity.this.overridePendingTransition(0, 0);
                        break;
                    case R.id.itemUpload:
                        break;
                    case R.id.itemProfile:
                        Intent intentProfile = AppService.getProfileIntent(UploadActivity.this);
                        startActivity(intentProfile);
                        UploadActivity.this.overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }

    /* Implement View.OnClickListener */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView_recipe) {
            pickFromGallery();
        } else if (view.getId() == R.id.buttonUploadRecipe) {
            uploadRecipe();
        }
    }
}
