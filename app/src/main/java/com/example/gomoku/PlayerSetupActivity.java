package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PlayerSetupActivity extends AppCompatActivity {

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


            Intent intent = new Intent(view.getContext(), GameActivity15.class);
            intent.putExtra("playerBlackName", playerBlackName);
            intent.putExtra("playerWhiteName", playerWhiteName);
            startActivity(intent);
        });
    }
}