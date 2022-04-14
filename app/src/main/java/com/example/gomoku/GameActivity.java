package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private int[][] imageViewsIds;

    private TextView gameInfo;

    private ImageView[][] imageViewsArray;
    private int[][] gameGrid;

    private String playerBlackName;
    private String playerWhiteName;
    private int counterBlackTurns = 0;
    private int counterWhiteTurns = 0;
    private boolean isBlackTurn = true;
    final private int BLACK = 0;
    final private int WHITE = 1;
    final private int EMPTY = 2;
    private int winner = EMPTY;
    final private int numberToWin = 5;
    private MyPair[] winningStones = new MyPair[numberToWin];
    private Button playAgainButton;
    private Button scoresButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if (bundle.getBoolean("isBoardSize15")) {
                setContentView(R.layout.activity_game_15);
                imageViewsIds = Boards.imageViews15Ids;
            } else {
                setContentView(R.layout.activity_game_19);
                imageViewsIds = Boards.imageViews19Ids;
            }
            imageViewsArray = new ImageView[imageViewsIds.length][imageViewsIds[0].length];
            gameGrid = new int[imageViewsArray.length][imageViewsArray[0].length];
            playerBlackName = bundle.getString("playerBlackName");
            playerWhiteName = bundle.getString("playerWhiteName");
        }

        gameInfo = (TextView) findViewById(R.id.gameInfo);
        setGameInfoToName(playerBlackName);
        setGameInfoToBlack();

        for(int i=0; i<imageViewsIds.length; i++) {
            for(int j=0; j<imageViewsIds[0].length; j++) {
                imageViewsArray[i][j] = (ImageView) findViewById(imageViewsIds[i][j]);
                ImageView currentImage = imageViewsArray[i][j];
                currentImage.setAlpha(0.0F);
                gameGrid[i][j] = EMPTY;
                int row = i;
                int column = j;

                currentImage.setOnClickListener(view -> {
                    if(view.getAlpha() != 1)
                    {
                        if(isBlackTurn) {
                            view.setAlpha(1.0F);
                            counterBlackTurns++;
                            isBlackTurn = false;
                            setGameInfoToName(playerWhiteName);
                            setGameInfoToWhite();
                            gameGrid[row][column] = BLACK;
//                            Log.d("position", row+ " ," + column);
                            if(checkWinningPosition(BLACK)) {
                                winner = BLACK;
                                setImageViewsArrayNonClickable();
                                colorWinningStones();
                                setGameInfoToWon(playerBlackName);
                                setGameInfoToBlack();
                                activateButtons();
                                addScoresToDb(playerBlackName, playerWhiteName, counterBlackTurns);
                            }
                        }
                        else {
                            if(bundle.getBoolean("isBoardSize15"))
                                currentImage.setImageResource(R.drawable.white_inverted);
                            else
                                currentImage.setImageResource(R.drawable.white_trim);
                            view.setAlpha(1.0F);
                            counterWhiteTurns++;
                            isBlackTurn = true;
                            setGameInfoToName(playerBlackName);
                            setGameInfoToBlack();
                            gameGrid[row][column] = WHITE;
//                            Log.d("position", row+ "," + column);
                            if(checkWinningPosition(WHITE)) {
                                winner = WHITE;
                                setImageViewsArrayNonClickable();
                                colorWinningStones();
                                setGameInfoToWon(playerWhiteName);
                                setGameInfoToWhite();
                                activateButtons();
                                addScoresToDb(playerWhiteName, playerBlackName, counterWhiteTurns);
                            }
                        }
                    }
                });
            }
        }
        Toast.makeText(this, "Game begins!", Toast.LENGTH_LONG).show();

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        scoresButton = (Button) findViewById(R.id.scoresButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        playAgainButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("playerBlackName", playerBlackName);
            intent.putExtra("playerWhiteName", playerWhiteName);
            intent.putExtra("isBoardSize15", imageViewsArray.length == 15);
            startActivity(intent);
        });

        scoresButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ScoresActivity.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    protected boolean checkWinningPosition(int color) {
        if(checkHorizontal(color))
            return true;
        else if(checkVertical(color))
            return true;
        else if(checkDiagonalToRight(color))
            return true;
        else if(checkDiagonalToLeft(color))
            return true;
        return false;
    }

    protected boolean checkHorizontal(int color) {
        int count;
        boolean hasWon = false;
        for(int i=0; i<gameGrid.length; i++) {
            count = 0;
            for(int j=0; j<gameGrid[0].length; j++) {
                if(gameGrid[i][j] != color) {
                    count = 0;
                }
                else {
                    winningStones[count] = new MyPair(i, j);
                    count++;
                    if(count == numberToWin) {
                        hasWon = true;
                        break;
                    }
                }
            }
            if(hasWon)
                break;
        }
//        Log.d("CHECK HORIZ", ""+ hasWon + " " + color);
        return hasWon;
    }

    protected boolean checkVertical(int color) {
        int count;
        boolean hasWon = false;
        for(int i=0; i<gameGrid.length; i++) {
            count = 0;
            for(int j=0; j<gameGrid[0].length; j++) {
                if(gameGrid[j][i] != color) {
                    count = 0;
                }
                else {
                    winningStones[count] = new MyPair(j, i);
                    count++;
                    if(count == numberToWin) {
                        hasWon = true;
                        break;
                    }
                }
            }
            if(hasWon)
                break;
        }
//        Log.d("CHECK VERT", ""+hasWon + " " + color);
        return hasWon;
    }

    protected boolean checkDiagonalToRight(int color) {
        boolean hasWon = false;
        for(int i=0; i<gameGrid.length; i++) {
            for(int j=0; j<gameGrid[0].length; j++) {
                for(int k=0; k<numberToWin; k++) {
                    int m = i + k;
                    int n = j + k;
                    if(m<0 || n<0 || m>=gameGrid.length || n>=gameGrid[0].length || gameGrid[m][n] != color) {
                        break;
                    }
                    winningStones[k] = new MyPair(m, n);
                    if(k==numberToWin-1) {
//                        Log.d("CHECK DIAG TO RIGHT", "true " + color);
                        hasWon = true;
                        return hasWon;
                    }
                }
            }
        }
//        Log.d("CHECK DIAG TO RIGHT", "false " + color);
        return hasWon;
    }

    protected boolean checkDiagonalToLeft(int color) {
        boolean hasWon = false;
        for(int i=0; i<gameGrid.length; i++) {
            for(int j=0; j<gameGrid[0].length; j++) {
                for(int k=0; k<numberToWin; k++) {
                    int m = i + k;
                    int n = j - k;
                    if(m<0 || n<0 || m>=gameGrid.length || n>=gameGrid[0].length || gameGrid[m][n] != color) {
                        break;
                    }
                    winningStones[k] = new MyPair(m, n);
                    if(k==numberToWin-1) {
//                        Log.d("CHECK DIAG TO LEFT", "true " + color);
                        hasWon = true;
                        return hasWon;
                    }
                }
            }
        }
//        Log.d("CHECK DIAG TO LEFT", "false " + color);
        return hasWon;
    }

    protected void setImageViewsArrayNonClickable() {
        for (int i = 0; i < imageViewsArray.length; i++) {
            for (int j = 0; j < imageViewsArray[0].length; j++) {
                imageViewsArray[i][j].setOnClickListener(null);
            }
        }
    }

    protected void colorWinningStones() {
        for(int i=0; i<numberToWin; i++) {
            imageViewsArray[winningStones[i].getRow()][winningStones[i].getColumn()]
                    .setBackgroundColor(getResources().getColor(R.color.dark));
        }
//        Log.d("WINNING STONES", winningStones[0].getRow() +":" + winningStones[0].getColumn()
//                + ", " + winningStones[2].getRow() +":" + winningStones[1].getColumn() + ", "
//                + winningStones[2].getRow() +":" + winningStones[2].getColumn() + ", "
//                + winningStones[3].getRow() +":" + winningStones[3].getColumn() + ", " +
//                winningStones[4].getRow() +":" + winningStones[4].getColumn() + ", ");
    }

    protected void setGameInfoToName(String name) {
        String msg = name + "'s turn";
        gameInfo.setText(msg);
    }

    protected void setGameInfoToWon(String name) {
        String msg = name + " has won!";
        gameInfo.setText(msg);
    }

    protected void setGameInfoToBlack() {
        gameInfo.setTextColor(getResources().getColor(R.color.blackPlayerTextColor));
    }

    protected void setGameInfoToWhite() {
        gameInfo.setTextColor(getResources().getColor(R.color.whitePlayerTextColor));
    }

    protected void activateButtons() {
        playAgainButton.setVisibility(View.VISIBLE);
        scoresButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
    }

    protected void addScoresToDb(String winner, String looser, int counterTurns) {
        SQLiteDatabase db;
        db = openOrCreateDatabase("gomoku", MODE_PRIVATE, null);
        String query = "CREATE TABLE IF NOT EXISTS scores(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "winner VARCHAR," +
                "looser VARCHAR," +
                "movesToWin INTEGER)";
        db.execSQL(query);

        query = "INSERT INTO scores VALUES (NULL, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, winner);
        statement.bindString(2, looser);
        statement.bindLong(3, counterTurns);
        statement.executeInsert();
    }
}