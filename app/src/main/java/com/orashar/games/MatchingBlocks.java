package com.orashar.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MatchingBlocks extends AppCompatActivity {

    RecyclerView blocksrv;
    List<MatchingBlocksItemObject> wordsList;
    int score;
    TextView scoretv;

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
        scoretv.setText("Score : " + score);


        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        final MatchingBlocksAdapter adapter = new MatchingBlocksAdapter(wordsList, screenHeight/5);
        blocksrv.setAdapter(adapter);

        adapter.setOnItemCLickListener(new MatchingBlocksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int isAnotherSelected = isAnotherBlockSelected();
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
}