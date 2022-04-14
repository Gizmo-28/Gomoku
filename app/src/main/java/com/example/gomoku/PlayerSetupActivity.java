package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayerSetupActivity extends AppCompatActivity {

    boolean isBoardSize15 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_setup);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Button submitButton = (Button)findViewById(R.id.submitButton);


        submitButton.setOnClickListener(view -> {
            EditText playerBlack = (EditText)findViewById(R.id.playerBlackName);
            String playerBlackName = playerBlack.getText().toString();
            if(playerBlackName.length() == 0)
                playerBlackName = "Black";

            EditText playerTwo = (EditText)findViewById(R.id.playerWhiteName);
            String playerWhiteName = playerTwo.getText().toString();
            if(playerWhiteName.length() == 0)
                playerWhiteName = "White";

//            Intent intent15 = new Intent(view.getContext(), GameActivity15.class);
//            Intent intent19 = new Intent(view.getContext(), GameActivity19.class);
//
//            if(isBoardSize15) {
//                intent15.putExtra("playerBlackName", playerBlackName);
//                intent15.putExtra("playerWhiteName", playerWhiteName);
//                startActivity(intent15);
//            } else {
//                intent19.putExtra("playerBlackName", playerBlackName);
//                intent19.putExtra("playerWhiteName", playerWhiteName);
//                startActivity(intent19);
//            }

            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("playerBlackName", playerBlackName);
            intent.putExtra("playerWhiteName", playerWhiteName);
            intent.putExtra("isBoardSize15", isBoardSize15);
            startActivity(intent);
        });

    }
    public void onRadioButtonClicked(View view) {
        if(view.getId() == R.id.button15)
            isBoardSize15 = true;
        else
            isBoardSize15 = false;
    }
}