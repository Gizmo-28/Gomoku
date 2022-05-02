package com.example.gomoku;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class AnalyzeActivity extends AppCompatActivity {

    private int[][] imageViewsIds;
    private ImageView[][] imageViewsArray;
    private ArrayList<MyPair> stonesInOrderList;
    private ArrayList<MyPair> winningStones;

    private GameInfoManipulator gameInfoManipulator;

    private String playerBlackName;
    private String playerWhiteName;

    boolean isBoardSize15;

    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isBoardSize15 = bundle.getBoolean("isBoardSize15");
            if (isBoardSize15) {
                setContentView(R.layout.activity_analyze_15);
                imageViewsIds = Boards.imageViews15Ids;
            } else {
                setContentView(R.layout.activity_analyze_19);
                imageViewsIds = Boards.imageViews19Ids;
            }
            imageViewsArray = new ImageView[imageViewsIds.length][imageViewsIds[0].length];
        }
        playerBlackName = bundle.getString("playerBlackName");
        playerWhiteName = bundle.getString("playerWhiteName");
        stonesInOrderList = (ArrayList<MyPair>) bundle.getSerializable("stonesInOrderList");
        winningStones = (ArrayList<MyPair>) bundle.getSerializable("winningStones");
        gameInfoManipulator = new GameInfoManipulator((TextView) findViewById(R.id.gameInfo));
        gameInfoManipulator.setGameInfoToName(playerBlackName);

        for(int i=0; i<imageViewsIds.length; i++) {
            for (int j = 0; j < imageViewsIds[0].length; j++) {
                imageViewsArray[i][j] = (ImageView) findViewById(imageViewsIds[i][j]);
                imageViewsArray[i][j].setAlpha(0.0F);
            }
        }

        index = 0;
        for (MyPair pair: stonesInOrderList)
        {
            if(index % 2 == 1) {
                int row = pair.getRow();
                int colomn = pair.getColumn();
                ImageView currentImage = imageViewsArray[row][colomn];
                if (isBoardSize15)
                    currentImage.setImageResource(R.drawable.white_inverted);
                else
                    currentImage.setImageResource(R.drawable.white_trim);
            }
            index++;
        }

        Button startButton = (Button) findViewById(R.id.startButton);
        Button prevButton = (Button) findViewById(R.id.previousButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button endButton = (Button) findViewById(R.id.endButton);

        startButton.setOnClickListener(view -> {
            index = 0;
            for (MyPair pair: stonesInOrderList)
            {
                int row = pair.getRow();
                int colomn = pair.getColumn();
                imageViewsArray[row][colomn].setAlpha(0.0F);
                gameInfoManipulator.setGameInfoToName(playerBlackName);
                gameInfoManipulator.setGameInfoToBlack();
            }
            uncolorWinningStones();
        });

        endButton.setOnClickListener(view -> {
            index = stonesInOrderList.size();
            for (MyPair pair: stonesInOrderList)
            {
                int row = pair.getRow();
                int colomn = pair.getColumn();
                imageViewsArray[row][colomn].setAlpha(1.0F);
            }
            colorWinningStones();
            changeNameToWinner();
        });

        prevButton.setOnClickListener(view -> {
            if (index > 0)
            {
                index--;
                int row = stonesInOrderList.get(index).getRow();
                int column = stonesInOrderList.get(index).getColumn();
                imageViewsArray[row][column].setAlpha(0.0F);
            }
            changeName();
            if (index == stonesInOrderList.size()-1)
            {
                uncolorWinningStones();
            }
        });

        nextButton.setOnClickListener(view -> {
            if(index < stonesInOrderList.size())
            {
                index++;
                int row = stonesInOrderList.get(index - 1).getRow();
                int column = stonesInOrderList.get(index - 1).getColumn();
                imageViewsArray[row][column].setAlpha(1.0F);
            }
            changeName();
            if (index == stonesInOrderList.size())
            {
                colorWinningStones();
                changeNameToWinner();
            }
        });

    }

    private void changeName()
    {
        if (index % 2 == 0)
        {
            gameInfoManipulator.setGameInfoToBlack();
            gameInfoManipulator.setGameInfoToName(playerBlackName);
        }
        else
        {
            gameInfoManipulator.setGameInfoToWhite();
            gameInfoManipulator.setGameInfoToName(playerWhiteName);
        }
    }

    private void changeNameToWinner()
    {
        if (index % 2 == 1)
        {
            gameInfoManipulator.setGameInfoToWon(playerBlackName);
            gameInfoManipulator.setGameInfoToBlack();
        }
        else
        {
            gameInfoManipulator.setGameInfoToWon(playerWhiteName);
            gameInfoManipulator.setGameInfoToWhite();
        }
    }

    private void colorWinningStones()
    {
        for (MyPair pair: winningStones)
        {
            int row = pair.getRow();
            int colomn = pair.getColumn();
            imageViewsArray[row][colomn].setBackgroundColor(getResources().getColor(R.color.dark));
        }
    }

    private void uncolorWinningStones()
    {
        for (MyPair pair: winningStones)
        {
            int row = pair.getRow();
            int colomn = pair.getColumn();
            imageViewsArray[row][colomn].setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }
}
