package com.example.sharecipes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharecipes.R;
import com.example.sharecipes.firebase.FirebaseAuthService;
import com.example.sharecipes.firebase.FirebaseDatabaseService;
import com.example.sharecipes.firebase.FirebaseStorageService;
import com.example.sharecipes.firebase.callback.FirebaseStorageListener;

import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    // Data Members
    private final int GALLERY_REQUEST_CODE = 1;
    private Uri uriImage = null;

    // Views
    private AppCompatImageView imageViewRecipe;
    private EditText editTextTitle;
    private EditText editTextIngredients;
    private Button buttonUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Views
        imageViewRecipe = findViewById(R.id.imageView_recipe);
        editTextTitle = findViewById(R.id.editText_recipe_title);
        editTextIngredients = findViewById(R.id.editText_recipe_ingredients);
        buttonUpload = findViewById(R.id.buttonUploadRecipe);

        // Events
        imageViewRecipe.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
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

        final String title = editTextTitle.getText().toString();
        final String ingredients = editTextIngredients.getText().toString();
        final String publisher = FirebaseAuthService.getInstance().getCurrentUser().getEmail();

        FirebaseStorageService.getInstance().upload("12345", uriImage, new FirebaseStorageListener() {
            @Override
            public void onSuccess(Uri uri) {

                Map<String, String> dictionary = new HashMap<String, String>();
                dictionary.put("title", title);
                dictionary.put("publisher", publisher);
                dictionary.put("ingredients", ingredients);
                dictionary.put("uri", uri.toString());

                FirebaseDatabaseService.getInstance().setValue("nadav", dictionary);
            }

            @Override
            public void onFailure() {
                Toast.makeText(AddRecipeActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
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
