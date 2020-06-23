package com.orashar.games;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordEcho extends AppCompatActivity {

    ImageButton playib, doneib;
    EditText answeret;
    TextView scoretv;
    TextToSpeech tts;
    List<String> wordsList;
    int position, score;

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

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                playib.setEnabled(true);
            }
        });
        tts.setLanguage(Locale.UK);

        position = 0;
        score = 0;
        startGame();
    }

    private void startGame() {
        playib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(wordsList.get(position), TextToSpeech.QUEUE_FLUSH, null);
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
                        answeret.setText("");
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

    private void finishGame() {
        Toast t = Toast.makeText(WordEcho.this, "Game Finished", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        answeret.setVisibility(View.GONE);
        playib.setEnabled(false);
        doneib.setEnabled(false);
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
}