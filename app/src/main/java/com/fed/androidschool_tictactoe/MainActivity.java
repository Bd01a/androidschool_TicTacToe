package com.fed.androidschool_tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements FieldHolder{

    private FieldView mFieldView;
    private LinearLayout mGameEndLayout;
    private TextView mWinnerTextView;

    private TextView mZeroCounter;
    private TextView mCrossCounter;

    private ValueAnimator mAnimatorAlphaGameEndLayout;
    private ValueAnimator mAnimatorTranslationGameEndLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFieldView = findViewById(R.id.field_view);
        mFieldView.setFieldHolder(this);
        mGameEndLayout = findViewById(R.id.game_end_layout);
        mGameEndLayout.setVisibility(View.INVISIBLE);
        Button mRegameButton = findViewById(R.id.btn_new_game);
        Button mExitButton = findViewById(R.id.btn_exit);
        mWinnerTextView = findViewById(R.id.text_view_winner);
        mZeroCounter = findViewById(R.id.text_view_zero_count);
        mCrossCounter = findViewById(R.id.text_view_cross_count);

        mAnimatorAlphaGameEndLayout = ValueAnimator.ofFloat(0,1);
        animatorInit(mAnimatorAlphaGameEndLayout);
        mAnimatorAlphaGameEndLayout.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGameEndLayout.setAlpha((float)animation.getAnimatedValue());
            }
        });

        mAnimatorTranslationGameEndLayout = ValueAnimator.ofFloat(
                getResources().getDimension(R.dimen.animation_translation_game_end_layout)
                ,0);
        animatorInit(mAnimatorTranslationGameEndLayout);
        mAnimatorTranslationGameEndLayout.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGameEndLayout.setTranslationY((float)animation.getAnimatedValue());
            }
        });

        mRegameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFieldView.setNullsForPlaces();
                mFieldView.setIsGameOn(true);
                mGameEndLayout.setVisibility(View.INVISIBLE);
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void animatorInit(ValueAnimator animator) {
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(0);
        animator.setDuration(getResources().getInteger(R.integer.animation_duration_game_end_layout));
    }

    @Override
    public void winnerDetermined() {
        mWinnerTextView.setText(mFieldView.getWinner());
        if(mFieldView.getWinnerId()==FieldView.CROSS){
            int newResult = Integer.valueOf(mCrossCounter.getText().toString())+1;
            mCrossCounter.setText(String.valueOf(newResult));
        }
        else  if(mFieldView.getWinnerId()==FieldView.ZERO){
            int newResult = Integer.valueOf(mZeroCounter.getText().toString())+1;
            mZeroCounter.setText(String.valueOf(newResult));
        }
        mGameEndLayout.setVisibility(View.VISIBLE);
        mAnimatorAlphaGameEndLayout.start();
        mAnimatorTranslationGameEndLayout.start();

    }
}
