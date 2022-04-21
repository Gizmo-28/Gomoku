package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {
    SQLiteDatabase db;
    ListView scoresList;
    Button dbCleanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        dbCleanButton = (Button) findViewById(R.id.dbCleanButton);
        dbCleanButton.setOnClickListener(view ->{
            String query = "DELETE FROM scores";
            db.execSQL(query);
            dbSelectAll();
        });

        scoresList = (ListView) findViewById(R.id.scoresList);
        dbSetup();
        dbSelectAll();
    }

    private void dbSetup(){
        db = openOrCreateDatabase("gomoku", MODE_PRIVATE, null);
        String query = "CREATE TABLE IF NOT EXISTS scores(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "winner VARCHAR," +
                "looser VARCHAR," +
                "movesToWin INTEGER)";
        db.execSQL(query);
    }

    private void dbSelectAll() {
        ArrayList<String> tempScores = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT winner, looser, movesToWin FROM scores ORDER BY movesToWin ASC",
                null);
        if(cursor.moveToFirst()) {
            do{
                @SuppressLint("Range") String winner = cursor.getString(cursor.getColumnIndex("winner"));
                @SuppressLint("Range") String looser = cursor.getString(cursor.getColumnIndex("looser"));
                @SuppressLint("Range") int movesToWin = cursor.getInt(cursor.getColumnIndex("movesToWin"));
                if(winner.contains("draw"))
                    tempScores.add("Player 1: " + winner.replace(" draw", "") +
                            "\nPlayer 2: " + looser + "\ndraw after: " + movesToWin + " moves");
                else
                    tempScores.add("Winner: " + winner + "\nLooser: " + looser + "\nwin in: " + movesToWin + " moves");
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                tempScores);
        scoresList.setAdapter(adapter);
        cursor.close();
    }
}