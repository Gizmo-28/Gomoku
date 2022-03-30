package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private final int[][] imageViewsIds = new int[][] {
            {
                    R.id.imageView0_0, R.id.imageView0_1, R.id.imageView0_2, R.id.imageView0_3,
                    R.id.imageView0_4, R.id.imageView0_5, R.id.imageView0_6, R.id.imageView0_7,
                    R.id.imageView0_8, R.id.imageView0_9, R.id.imageView0_10, R.id.imageView0_11,
                    R.id.imageView0_12, R.id.imageView0_13, R.id.imageView0_14,
            },
            {
                    R.id.imageView1_0, R.id.imageView1_1, R.id.imageView1_2, R.id.imageView1_3,
                    R.id.imageView1_4, R.id.imageView1_5, R.id.imageView1_6, R.id.imageView1_7,
                    R.id.imageView1_8, R.id.imageView1_9, R.id.imageView1_10, R.id.imageView1_11,
                    R.id.imageView1_12, R.id.imageView1_13, R.id.imageView1_14,
            },
            {
                    R.id.imageView2_0, R.id.imageView2_1, R.id.imageView2_2, R.id.imageView2_3,
                    R.id.imageView2_4, R.id.imageView2_5, R.id.imageView2_6, R.id.imageView2_7,
                    R.id.imageView2_8, R.id.imageView2_9, R.id.imageView2_10, R.id.imageView2_11,
                    R.id.imageView2_12, R.id.imageView2_13, R.id.imageView2_14,
            },
            {
                    R.id.imageView3_0, R.id.imageView3_1, R.id.imageView3_2, R.id.imageView3_3,
                    R.id.imageView3_4, R.id.imageView3_5, R.id.imageView3_6, R.id.imageView3_7,
                    R.id.imageView3_8, R.id.imageView3_9, R.id.imageView3_10, R.id.imageView3_11,
                    R.id.imageView3_12, R.id.imageView3_13, R.id.imageView3_14,
            },
            {
                    R.id.imageView4_0, R.id.imageView4_1, R.id.imageView4_2, R.id.imageView4_3,
                    R.id.imageView4_4, R.id.imageView4_5, R.id.imageView4_6, R.id.imageView4_7,
                    R.id.imageView4_8, R.id.imageView4_9, R.id.imageView4_10, R.id.imageView4_11,
                    R.id.imageView4_12, R.id.imageView4_13, R.id.imageView4_14,
            },
            {
                    R.id.imageView5_0, R.id.imageView5_1, R.id.imageView5_2, R.id.imageView5_3,
                    R.id.imageView5_4, R.id.imageView5_5, R.id.imageView5_6, R.id.imageView5_7,
                    R.id.imageView5_8, R.id.imageView5_9, R.id.imageView5_10, R.id.imageView5_11,
                    R.id.imageView5_12, R.id.imageView5_13, R.id.imageView5_14,
            },
            {
                    R.id.imageView6_0, R.id.imageView6_1, R.id.imageView6_2, R.id.imageView6_3,
                    R.id.imageView6_4, R.id.imageView6_5, R.id.imageView6_6, R.id.imageView6_7,
                    R.id.imageView6_8, R.id.imageView6_9, R.id.imageView6_10, R.id.imageView6_11,
                    R.id.imageView6_12, R.id.imageView6_13, R.id.imageView6_14,
            },
            {
                    R.id.imageView7_0, R.id.imageView7_1, R.id.imageView7_2, R.id.imageView7_3,
                    R.id.imageView7_4, R.id.imageView7_5, R.id.imageView7_6, R.id.imageView7_7,
                    R.id.imageView7_8, R.id.imageView7_9, R.id.imageView7_10, R.id.imageView7_11,
                    R.id.imageView7_12, R.id.imageView7_13, R.id.imageView7_14,
            },
            {
                    R.id.imageView8_0, R.id.imageView8_1, R.id.imageView8_2, R.id.imageView8_3,
                    R.id.imageView8_4, R.id.imageView8_5, R.id.imageView8_6, R.id.imageView8_7,
                    R.id.imageView8_8, R.id.imageView8_9, R.id.imageView8_10, R.id.imageView8_11,
                    R.id.imageView8_12, R.id.imageView8_13, R.id.imageView8_14,
            },
            {
                    R.id.imageView9_0, R.id.imageView9_1, R.id.imageView9_2, R.id.imageView9_3,
                    R.id.imageView9_4, R.id.imageView9_5, R.id.imageView9_6, R.id.imageView9_7,
                    R.id.imageView9_8, R.id.imageView9_9, R.id.imageView9_10, R.id.imageView9_11,
                    R.id.imageView9_12, R.id.imageView9_13, R.id.imageView9_14,
            },
            {
                    R.id.imageView10_0, R.id.imageView10_1, R.id.imageView10_2, R.id.imageView10_3,
                    R.id.imageView10_4, R.id.imageView10_5, R.id.imageView10_6, R.id.imageView10_7,
                    R.id.imageView10_8, R.id.imageView10_9, R.id.imageView10_10, R.id.imageView10_11,
                    R.id.imageView10_12, R.id.imageView10_13, R.id.imageView10_14,
            },
            {
                    R.id.imageView11_0, R.id.imageView11_1, R.id.imageView11_2, R.id.imageView11_3,
                    R.id.imageView11_4, R.id.imageView11_5, R.id.imageView11_6, R.id.imageView11_7,
                    R.id.imageView11_8, R.id.imageView11_9, R.id.imageView11_10, R.id.imageView11_11,
                    R.id.imageView11_12, R.id.imageView11_13, R.id.imageView11_14,
            },
            {
                    R.id.imageView12_0, R.id.imageView12_1, R.id.imageView12_2, R.id.imageView12_3,
                    R.id.imageView12_4, R.id.imageView12_5, R.id.imageView12_6, R.id.imageView12_7,
                    R.id.imageView12_8, R.id.imageView12_9, R.id.imageView12_10, R.id.imageView12_11,
                    R.id.imageView12_12, R.id.imageView12_13, R.id.imageView12_14,
            },
            {
                    R.id.imageView13_0, R.id.imageView13_1, R.id.imageView13_2, R.id.imageView13_3,
                    R.id.imageView13_4, R.id.imageView13_5, R.id.imageView13_6, R.id.imageView13_7,
                    R.id.imageView13_8, R.id.imageView13_9, R.id.imageView13_10, R.id.imageView13_11,
                    R.id.imageView13_12, R.id.imageView13_13, R.id.imageView13_14,
            },
            {
                    R.id.imageView14_0, R.id.imageView14_1, R.id.imageView14_2, R.id.imageView14_3,
                    R.id.imageView14_4, R.id.imageView14_5, R.id.imageView14_6, R.id.imageView14_7,
                    R.id.imageView14_8, R.id.imageView14_9, R.id.imageView14_10, R.id.imageView14_11,
                    R.id.imageView14_12, R.id.imageView14_13, R.id.imageView14_14,
            },
    };

    private TextView gameInfo;

    private final ImageView[][] imageViewsArray = new ImageView[imageViewsIds.length][imageViewsIds[0].length];
    private int[][] gameGrid = new int[imageViewsArray.length][imageViewsArray[0].length];

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
        setContentView(R.layout.activity_game);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
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
                            currentImage.setImageResource(R.drawable.white_inverted);
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
        gameInfo.setTextColor(getResources().getColor(R.color.black));
    }

    protected void setGameInfoToWhite() {
        gameInfo.setTextColor(getResources().getColor(R.color.white));
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