package com.example.gomoku;

import android.graphics.Color;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameInfoManipulator extends AppCompatActivity {

    private final TextView gameInfo;

    public GameInfoManipulator(TextView gameInfo)
    {
        this.gameInfo = gameInfo;
    }

    protected void setGameInfoToName(String name) {
        String msg = name + "'s turn";
        gameInfo.setText(msg);
    }

    protected void setGameInfoToWon(String name) {
        String msg = name + " has won!";
        gameInfo.setText(msg);
    }

    protected void setGameInfoToDraw() {
        String msg = "Game drawn!";
        gameInfo.setText(msg);
    }

    protected void setGameInfoToBlack() {
        gameInfo.setTextColor(Color.BLACK);
    }

    protected void setGameInfoToWhite() {
        gameInfo.setTextColor(Color.WHITE);
    }
}
