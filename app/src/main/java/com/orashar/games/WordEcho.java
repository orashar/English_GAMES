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
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WordEcho extends AppCompatActivity {

    ImageButton playib, doneib;
    EditText answeret;
    TextView scoretv, timertv, animatedTimer, currentTimertv;
    TextToSpeech tts;
    List<String> wordsList;
    int position, score, maxWidth;
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
        currentTimertv = findViewById(R.id.current_question_tv);
        currentTimertv.setText((position+1)+"/"+wordsList.size());

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
        answeret.setFilters(new InputFilter[] {new InputFilter.LengthFilter(wordsList.get(position).length())});
        playib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(wordsList.get(position), TextToSpeech.QUEUE_FLUSH, onlineSpeech);
            }
        });
        doneib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < wordsList.size()-1) {
                    String input = answeret.getText().toString();
                    if (input.toLowerCase().equals(wordsList.get(position).toLowerCase())) {
                        Toast t = Toast.makeText(WordEcho.this, "Correct answer", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        position++;
                        score += 10;
                        scoretv.setText("Score : " + score);
                        currentTimertv.setText((position+1)+"/"+wordsList.size());
                        answeret.setText("");
                        answeret.setFilters(new InputFilter[] {new InputFilter.LengthFilter(wordsList.get(position).length())});
                    }else{
                        Toast t = Toast.makeText(WordEcho.this, "Wrong answer", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                } else{
                    finishGame();
                }
            }
        });
    }

    private void startTimer() {
        timertv = findViewById(R.id.timer_tv);
        timertv.setText("20");
        cdt = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {
                timertv.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                finishGame();
                Toast.makeText(WordEcho.this, "Time Out", Toast.LENGTH_SHORT).show();
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
                maxWidth = ((View)animatedTimer.getParent()).getMeasuredWidth();
                widthAnimator = ValueAnimator.ofInt(maxWidth, 0);
                widthAnimator.setDuration(40000);
                widthAnimator.setInterpolator(null);
                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        animatedTimer.getLayoutParams().width = (int)valueAnimator.getAnimatedValue();
                        animatedTimer.requestLayout();
                        Log.v("widthAniamtorValue", "value:"+(int)valueAnimator.getAnimatedValue());                    }
                });
                cdt.start();
                widthAnimator.start();
                colorAnimator.start();
            }
        });

    }

    private void finishGame() {
        currentTimertv.setText("--/--");
        Toast t = Toast.makeText(WordEcho.this, "Game Finished", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishGame();
    }
}