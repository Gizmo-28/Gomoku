package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Button playButton = (Button)findViewById(R.id.playButton);
        Button scoresMainButton = (Button)findViewById(R.id.scoresMainButton);

        playButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PlayerSetupActivity.class);
            startActivity(intent);
        });

        scoresMainButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ScoresActivity.class);
            startActivity(intent);
        });
    }

}