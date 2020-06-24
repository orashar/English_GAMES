package com.orashar.games;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WordEcho extends AppCompatActivity {

    private boolean isActivityAlive = false;
    public static final int DURATION_FOR_ONE_QUESTION = 15000;

    ImageView playib, doneib;
    EditText answeret;
    TextView scoretv, timertv, animatedTimer, currentQuestiontv, livestv;
    TextToSpeech tts;
    List<String> wordsList;
    int position, score, maxWidth, remainingLives;
    HashMap<String, String> onlineSpeech;
    CountDownTimer cdt;
    ValueAnimator widthAnimator, colorAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_echo);

        wordsList = new ArrayList<>();

        getWordsList();

        answeret = findViewById(R.id.answer_et);
        answeret.setText("");
        playib = findViewById(R.id.play_ib);
        doneib = findViewById(R.id.done_ib);
        playib.setEnabled(false);

        scoretv = findViewById(R.id.score_tv);
        scoretv.setText("Score : 0");
        livestv = findViewById(R.id.lives_tv);
        currentQuestiontv = findViewById(R.id.current_question_tv);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                playib.setEnabled(true);
            }
        });
        onlineSpeech = new HashMap<>();
        onlineSpeech.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");
        tts.setLanguage(Locale.UK);

        position = 0;
        score = 0;
        startGame();
    }

    private void startGame() {
        startTimer();
        remainingLives = 3;
        int answerLength = wordsList.get(position).length();
        answeret.setFilters(new InputFilter[] {new InputFilter.LengthFilter(answerLength)});
        answeret.setHint(answerLength + " letters");
        currentQuestiontv.setText((position+1)+"/"+wordsList.size());
        livestv.setText(remainingLives + " lives");
        playib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeView(playib, 200, 0);
                tts.speak(wordsList.get(position), TextToSpeech.QUEUE_FLUSH, onlineSpeech);
            }
        });
        doneib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < wordsList.size()-1) {
                    String input = answeret.getText().toString();
                    if (input.toLowerCase().equals(wordsList.get(position).toLowerCase())) {
                        handleCorrectAnswer();
                    }else{
                        handleWrongAnswer();
                    }
                } else{
                    finishGame();
                }
            }
        });
    }

    private void handleCorrectAnswer(){
        answeret.setText("");
        showToast("Correct answer", Gravity.CENTER);
        score += 10;
        scoretv.setText("Score : " + score);
        nextQuestion();
    }

    private void  handleWrongAnswer(){
        showToast("Wrong Answer", Gravity.CENTER);
    }

    private void nextQuestion(){
        position++;
        currentQuestiontv.setText((position+1)+"/"+wordsList.size());
        int answerLength = wordsList.get(position).length();
        answeret.setFilters(new InputFilter[] {new InputFilter.LengthFilter(answerLength)});
        answeret.setHint(answerLength + " letters");
        startTimer();
    }

    private void showToast(String body, int location) {
        if (isActivityAlive) {
            Toast t = Toast.makeText(WordEcho.this, body, Toast.LENGTH_SHORT);
            t.setGravity(location, 0, 0);
            t.show();
        }
    }

    private void startTimer() {
        timertv = findViewById(R.id.timer_tv);
        timertv.setText("20");
        if(cdt != null) cdt.cancel();
        if(widthAnimator != null) widthAnimator.cancel();
        if(colorAnimator != null) colorAnimator.cancel();
        cdt = new CountDownTimer(DURATION_FOR_ONE_QUESTION, 1000) {
            @Override
            public void onTick(long l) {
                timertv.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                if(position == wordsList.size()-1) {
                    finishGame();
                } else {
                    if (remainingLives <= 0) {
                        finishGame();
                    } else {
                        updateLives(-1);
                        nextQuestion();
                    }
                }
                showToast("Time Out", Gravity.BOTTOM);
            }
        };

        animatedTimer = findViewById(R.id.animated_timer);

        int colorFrom = Color.GREEN;
        int colorTo = Color.RED;
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimator.setDuration(DURATION_FOR_ONE_QUESTION*2);
        colorAnimator.setInterpolator(new DecelerateInterpolator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedTimer.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });

        animatedTimer.post(new Runnable() {
            @Override
            public void run() {
                maxWidth = ((View)animatedTimer.getParent()).getMeasuredWidth();
                widthAnimator = ValueAnimator.ofInt(maxWidth, 0);
                widthAnimator.setDuration(DURATION_FOR_ONE_QUESTION*2);
                widthAnimator.setInterpolator(null);
                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        animatedTimer.getLayoutParams().width = (int)valueAnimator.getAnimatedValue();
                        animatedTimer.requestLayout();                }
                });
                cdt.start();
                widthAnimator.start();
                colorAnimator.start();
            }
        });

    }

    private void updateLives(int i) {
        remainingLives += i;
        livestv.setText(remainingLives + " lives");
    }

    private void finishGame() {
        currentQuestiontv.setText("--/--");
        showToast("Game Finished", Gravity.BOTTOM);
        answeret.setVisibility(View.GONE);
        playib.setEnabled(false);
        doneib.setEnabled(false);
        widthAnimator.cancel();
        colorAnimator.cancel();
        cdt.cancel();
    }

    private void getWordsList() {
        wordsList.add("Apple");
        wordsList.add("Banana");
        wordsList.add("Cat");
        wordsList.add("Dog");
        wordsList.add("Elephant");
        wordsList.add("Fish");
        wordsList.add("Guinea");
        wordsList.add("Hen");
        wordsList.add("Ink");
        wordsList.add("Jellyfish");
    }

    private void shakeView(View view, int duration, int offset){
        Animation animation = new RotateAnimation(-30, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setRepeatMode(Animation.INFINITE);
        view.startAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityAlive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityAlive = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishGame();
    }
}