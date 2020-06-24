package com.orashar.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.renderscript.Sampler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MatchingBlocks extends AppCompatActivity {

    public static final int GAME_DURATION = 40000;

    RecyclerView blocksrv;
    List<MatchingBlocksItemObject> wordsList;
    int score, maxHeight;
    TextView scoretv, timertv, animatedTimer;
    CountDownTimer cdt;
    ValueAnimator widthAnimator, colorAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_blocks);

        wordsList = new ArrayList<>();
        getWordsList();

        blocksrv = findViewById(R.id.blocks_rv);
        blocksrv.setHasFixedSize(true);
        blocksrv.setLayoutManager(new GridLayoutManager(this, 3));

        score = 0;
        scoretv = findViewById(R.id.score_tv);

        timertv = findViewById(R.id.timer_tv);


        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        final MatchingBlocksAdapter adapter = new MatchingBlocksAdapter(wordsList, screenHeight/6);
        blocksrv.setAdapter(adapter);

        adapter.setOnItemCLickListener(new MatchingBlocksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int isAnotherSelected = isAnotherBlockSelected();
                shakeView(v, 10, 0);
                if (isAnotherSelected >= 0) {
                    wordsList.get(position).setSelected(false);
                    wordsList.get(isAnotherSelected).setSelected(false);
                    if(wordsList.get(position).getAnswer().equals(wordsList.get(isAnotherSelected).getText())){
                        wordsList.get(position).setMatched(true);
                        wordsList.get(isAnotherSelected).setMatched(true);
                        score += 10;
                        scoretv.setText("Score : " + score);
                        Toast toast = Toast.makeText(getApplicationContext(), "Correct match", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Incorrect match", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    adapter.notifyDataSetChanged();
                    if(score >= 60) finishGame();
                } else {
                    wordsList.get(position).setSelected(true);
                    adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "First selection", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

    }

    private void startGame(){

    }

    private void updateScore(){
        scoretv.setText("Score : " + score);
    }

    private void startTimer(){

        timertv.setText("20");
        cdt = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {
                timertv.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                finishGame();
                Toast.makeText(MatchingBlocks.this, "Time Out", Toast.LENGTH_SHORT).show();
            }
        };

        animatedTimer = findViewById(R.id.animated_timer);

        int colorFrom = Color.GREEN;
        int colorTo = Color.RED;
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimator.setDuration(40000);
        colorAnimator.setInterpolator(new DecelerateInterpolator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.v("ColorAniamtorValue", "value:"+(int)valueAnimator.getAnimatedValue());
                animatedTimer.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });

        animatedTimer.post(new Runnable() {
            @Override
            public void run() {
                maxHeight = ((View)animatedTimer.getParent()).getMeasuredHeight();
                widthAnimator = ValueAnimator.ofInt(0, maxHeight);
                widthAnimator.setDuration(40000);
                widthAnimator.setInterpolator(null);
                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        animatedTimer.getLayoutParams().height = (int)valueAnimator.getAnimatedValue();
                        animatedTimer.requestLayout();
                    }
                });
                cdt.start();
                widthAnimator.start();
                colorAnimator.start();
            }
        });
    }


    private void shakeView(View view, int duration, int offset){
        Animation animation = new RotateAnimation(0, 30, 0, 0);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

    private void finishGame() {
        cdt.cancel();
        widthAnimator.cancel();
        colorAnimator.cancel();
        animatedTimer.getLayoutParams().height = maxHeight;
        animatedTimer.requestLayout();
        blocksrv.setVisibility(View.GONE);
        Toast.makeText(this, "Game Finished", Toast.LENGTH_SHORT).show();
        cdt.cancel();
    }

    private int isAnotherBlockSelected() {
        for(int i = 0; i < wordsList.size(); i++){
            if(wordsList.get(i).isSelected()) return i;
        }
        return -1;
    }

    private void getWordsList() {
        wordsList.add(new MatchingBlocksItemObject("Hello", "World", false, false));
        wordsList.add(new MatchingBlocksItemObject("World", "Hello", false, false));
        wordsList.add(new MatchingBlocksItemObject("C", "++", false, false));
        wordsList.add(new MatchingBlocksItemObject("++", "C", false, false));
        wordsList.add(new MatchingBlocksItemObject("Java", "Android", false, false));
        wordsList.add(new MatchingBlocksItemObject("Android", "Java", false, false));
        wordsList.add(new MatchingBlocksItemObject("Python", "Easy", false, false));
        wordsList.add(new MatchingBlocksItemObject("Easy", "Python", false, false));
        wordsList.add(new MatchingBlocksItemObject("DS", "Algo", false, false));
        wordsList.add(new MatchingBlocksItemObject("Algo", "DS", false, false));
        wordsList.add(new MatchingBlocksItemObject("Competetive", "Necessary", false, false));
        wordsList.add(new MatchingBlocksItemObject("Necessary", "Competetive", false, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishGame();
    }
}