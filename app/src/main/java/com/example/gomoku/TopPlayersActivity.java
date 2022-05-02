package com.example.gomoku;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class TopPlayersActivity extends AppCompatActivity {
    SQLiteDatabase db;
    ListView playersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_players);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        playersList = (ListView) findViewById(R.id.playersList);
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
        Cursor cursor = db.rawQuery("SELECT player, AVG(win) AS ratio FROM (SELECT winner AS player, 1.0 AS win from scores " +
                        "UNION ALL SELECT looser, 0 FROM scores) t GROUP BY player ORDER BY ratio DESC",
                null);
        if(cursor.moveToFirst()) {
            do{
                @SuppressLint("Range") String winner = cursor.getString(cursor.getColumnIndex("player"));
                @SuppressLint("Range") double ratio = cursor.getDouble(cursor.getColumnIndex("ratio"));

                if(winner.contains("draw")) {/*do nothing*/}
                else
                    tempScores.add("Player: " + winner + "\nWins/Loses ratio: " + String.format("%.2f", ratio*100) +"%");
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                tempScores);
        playersList.setAdapter(adapter);
        cursor.close();
    }
}
