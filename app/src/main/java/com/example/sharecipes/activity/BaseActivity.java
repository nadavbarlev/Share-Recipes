package com.example.sharecipes.activity;

import android.view.View;
import android.widget.FrameLayout;

import com.example.sharecipes.R;
import com.example.sharecipes.util.ui.HorizontalDottedProgress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BaseActivity extends AppCompatActivity {

    /* Data Members */
    private ConstraintLayout         mConstraintLayout;
    private FrameLayout              mFrameLayout;
    private HorizontalDottedProgress mProgressBar;

    /* Override Methods*/
    @Override
    public void setContentView(int layoutResID) {

        /* Gets Views */
        mConstraintLayout = (ConstraintLayout)getLayoutInflater().inflate(R.layout.activity_base, null);
        mFrameLayout = mConstraintLayout.findViewById(R.id.activity_content);
        mProgressBar = mConstraintLayout.findViewById(R.id.progress_bar);

        // Associate layoutResID to mFrameLayout
        getLayoutInflater().inflate(layoutResID, mFrameLayout, true);

        super.setContentView(layoutResID);
    }

    /* Methods */
    public void showProgressBar(boolean visibility) {
        mProgressBar.clearAnimation();
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }


    // TODO: 1. Read data firebase
    // TODO: 2. Merge firebase data and food2fork data
    // TODO: 3. Icons color at Tab bar
    // TODO: 4. Show Progress bar in base activity
    // TODO: 5. Intents at Tab bar
}
