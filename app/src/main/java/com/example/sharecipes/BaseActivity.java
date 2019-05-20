package com.example.sharecipes;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.sharecipes.util.HorizontalDottedProgress;

public abstract class BaseActivity extends AppCompatActivity {

    /* Data Members */
    private ConstraintLayout        mConstraintLayout;
    private FrameLayout             mFrameLayout;
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
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
