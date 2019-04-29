package com.example.sharecipes.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.example.sharecipes.R;

public class HorizontalDottedProgress extends View {

    // Actual dot radius
    private int mDotRadius = 5;

    // Bounced Dot Radius
    private int mBounceDotRadius = 8;

    // To get identified in which position dot has to bounce
    private int  mDotPosition;

    // Specify how many dots you need in a progressbar
    private int mDotAmount = 10;

    /* Constructors */
    public HorizontalDottedProgress(Context context) {
        super(context);
    }

    public HorizontalDottedProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalDottedProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* Implement View Methods */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw your customized dot on the canvas
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        createDot(canvas,paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Animation called when attaching to the window
        startAnimation();
    }

    /* Methods */
    private void createDot(Canvas canvas, Paint paint) {
        for(int i = 0; i < mDotAmount; i++ ){
            if(i == mDotPosition){
                canvas.drawCircle(10+(i*20), mBounceDotRadius, mBounceDotRadius, paint);
            }else {
                canvas.drawCircle(10+(i*20), mBounceDotRadius, mDotRadius, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Calculate the view width
        int calculatedWidth = (20*9);
        int width  = calculatedWidth;
        int height = (mBounceDotRadius*2);

        setMeasuredDimension(width, height);
    }

    private void startAnimation() {
        BounceAnimation bounceAnimation = new BounceAnimation();
        bounceAnimation.setDuration(100);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setInterpolator(new LinearInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {
                mDotPosition++;
                if (mDotPosition == mDotAmount) {
                    mDotPosition = 0;
                }
            }
        });
        startAnimation(bounceAnimation);
    }

    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            invalidate();
        }
    }
}
