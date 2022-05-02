package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class GameActivity extends AppCompatActivity {

    private int[][] imageViewsIds;

    private TextView gameInfo;
    private GameInfoManipulator gameInfoManipulator;

    private ImageView[][] imageViewsArray;
    private int[][] gameGrid;

    private ArrayList<MyPair> stonesInOrderList = new ArrayList<>();

    private final int STANDARD_TIME = 1 * 60 * 1000;
    private final int TIME_WITH_BONUS = 1 * 60 * 1000;
    private final int BONUS_TIME = 5 * 1000;

    private boolean withBonus = false;
    private CountDownTimer whiteTimer;
    private CountDownTimer blackTimer;

    private TextView whiteTimerLabel;
    private TextView blackTimerLabel;

    private boolean whiteTimerActive = false;
    private boolean blackTimerActive = false;

    private long lastWhiteTimerValue = 0;
    private long lastBlackTimerValue = 0;

    private long startWhiteTimerValue = 0;
    private long startBlackTimerValue = 0;

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
    private Button analyzeButton;

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

            if (bundle.getBoolean("timeWithBonus")) {
                initlializeTimers(TIME_WITH_BONUS);
                lastWhiteTimerValue = TIME_WITH_BONUS;
                lastBlackTimerValue = TIME_WITH_BONUS;
                startWhiteTimerValue = TIME_WITH_BONUS;
                startBlackTimerValue = TIME_WITH_BONUS;
                withBonus = true;
            } else {
                initlializeTimers(STANDARD_TIME);
                lastWhiteTimerValue = STANDARD_TIME;
                lastBlackTimerValue = STANDARD_TIME;
                startWhiteTimerValue = STANDARD_TIME;
                startBlackTimerValue = STANDARD_TIME;
                withBonus = false;
            }

            imageViewsArray = new ImageView[imageViewsIds.length][imageViewsIds[0].length];
            gameGrid = new int[imageViewsArray.length][imageViewsArray[0].length];
            playerBlackName = bundle.getString("playerBlackName");
            playerWhiteName = bundle.getString("playerWhiteName");
        }

        whiteTimerLabel = (TextView) findViewById(R.id.whiteTimer);
        blackTimerLabel = (TextView) findViewById(R.id.blackTimer);

        if(withBonus) {
            startBlackTimerValue += BONUS_TIME;
            startWhiteTimerValue += BONUS_TIME;
        }

        gameInfoManipulator = new GameInfoManipulator((TextView) findViewById(R.id.gameInfo));
        gameInfoManipulator.setGameInfoToName(playerBlackName);
        gameInfoManipulator.setGameInfoToBlack();

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
                            if (withBonus) {
                                long responseTime = startBlackTimerValue - lastBlackTimerValue;
                                if (responseTime <= BONUS_TIME) {
                                    lastBlackTimerValue += BONUS_TIME - responseTime;
                                    updateTimer(blackTimer, lastBlackTimerValue);
                                }
                            }
                            blackTimerActive = false;
                            blackTimer.cancel();
                            initlializeBlackTimer(lastBlackTimerValue);
                            whiteTimerActive = true;
                            startWhiteTimerValue = lastWhiteTimerValue;
                            whiteTimer.start();
                            view.setAlpha(1.0F);
                            counterBlackTurns++;
                            isBlackTurn = false;
                            gameInfoManipulator.setGameInfoToName(playerWhiteName);
                            gameInfoManipulator.setGameInfoToWhite();
                            gameGrid[row][column] = BLACK;
                            stonesInOrderList.add(new MyPair(row, column));
//                            Log.d("position", row+ " ," + column);
                            if(checkWinningPosition(BLACK)) {
                                whiteTimer.cancel();
                                blackTimer.cancel();
                                winner = BLACK;
                                setImageViewsArrayNonClickable();
                                colorWinningStones();
                                gameInfoManipulator.setGameInfoToWon(playerBlackName);
                                gameInfoManipulator.setGameInfoToBlack();
                                activateButtons();
                                addScoresToDbIfWon(playerBlackName, playerWhiteName, counterBlackTurns);
                            }
                        }
                        else {
                            if (withBonus) {
                                long responseTime = startWhiteTimerValue - lastWhiteTimerValue;
                                if (responseTime <= BONUS_TIME) {
                                    lastWhiteTimerValue += BONUS_TIME - responseTime;
                                    updateTimer(whiteTimer, lastWhiteTimerValue);
                                }
                            }
                            whiteTimerActive = false;
                            whiteTimer.cancel();
                            initlializeWhiteTimer(lastWhiteTimerValue);
                            blackTimerActive = true;
                            startBlackTimerValue = lastBlackTimerValue;
                            blackTimer.start();
                            if(bundle.getBoolean("isBoardSize15"))
                                currentImage.setImageResource(R.drawable.white_inverted);
                            else
                                currentImage.setImageResource(R.drawable.white_trim);
                            view.setAlpha(1.0F);
                            counterWhiteTurns++;
                            isBlackTurn = true;
                            gameInfoManipulator.setGameInfoToName(playerBlackName);
                            gameInfoManipulator.setGameInfoToBlack();
                            gameGrid[row][column] = WHITE;
                            stonesInOrderList.add(new MyPair(row, column));
//                            Log.d("position", row+ "," + column);
                            if(checkWinningPosition(WHITE)) {
                                whiteTimer.cancel();
                                blackTimer.cancel();
                                winner = WHITE;
                                setImageViewsArrayNonClickable();
                                colorWinningStones();
                                gameInfoManipulator.setGameInfoToWon(playerWhiteName);
                                gameInfoManipulator.setGameInfoToWhite();
                                activateButtons();
                                addScoresToDbIfWon(playerWhiteName, playerBlackName, counterWhiteTurns);
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
        analyzeButton = (Button) findViewById(R.id.analyzeButton);

        playAgainButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("playerBlackName", playerBlackName);
            intent.putExtra("playerWhiteName", playerWhiteName);
            intent.putExtra("isBoardSize15", imageViewsArray.length == 15);
            startActivity(intent);
        });

        scoresButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Draw offered");
            builder.setMessage("Do you accept draw?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    whiteTimer.cancel();
                    blackTimer.cancel();
                    setImageViewsArrayNonClickable();
                    gameInfoManipulator.setGameInfoToDraw();
                    activateButtons();
                    int movesNum = counterBlackTurns > counterWhiteTurns ? counterBlackTurns : counterWhiteTurns;
                    addScoresToDbIfDrawn(playerWhiteName, playerBlackName, movesNum);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Draw declined!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        });

        exitButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });

        analyzeButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AnalyzeActivity.class);
            intent.putExtra("playerBlackName", playerBlackName);
            intent.putExtra("playerWhiteName", playerWhiteName);
            intent.putExtra("isBoardSize15", bundle.getBoolean("isBoardSize15"));
            intent.putExtra("stonesInOrderList", stonesInOrderList);
            ArrayList<MyPair> temp = new ArrayList<>(Arrays.asList(winningStones));
            intent.putExtra("winningStones", temp);
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

    protected void activateButtons() {
        playAgainButton.setVisibility(View.VISIBLE);
        scoresButton.setText("SCORES");
        scoresButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ScoresActivity.class);
            startActivity(intent);
        });
        analyzeButton.setVisibility(View.VISIBLE);
    }

    protected void addScoresToDbIfWon(String winner, String looser, int counterTurns) {
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

    protected void addScoresToDbIfDrawn(String winner, String looser, int counterTurns) {
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
        statement.bindString(1, winner + " draw");
        statement.bindString(2, looser);
        statement.bindLong(3, counterTurns);
        statement.executeInsert();
    }

    protected void initlializeTimers(long millis) {
        initlializeWhiteTimer(millis);
        initlializeBlackTimer(millis);
    }

    protected void initlializeWhiteTimer(long millis) {
        whiteTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {
                updateTimer(this ,l);
            }

            @Override
            public void onFinish() {
                timeOut(this);
            }
        };
    }

    protected void initlializeBlackTimer(long millis) {
        blackTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {
                updateTimer(this ,l);
            }

            @Override
            public void onFinish() {
                timeOut(this);
            }
        };
    }

    protected void updateTimer(CountDownTimer timer, long millis) {
        if (timer == whiteTimer) {
            lastWhiteTimerValue = millis;
            whiteTimerLabel.setText(getTimerLabel(millis));
        } else {
            lastBlackTimerValue = millis;
            blackTimerLabel.setText((getTimerLabel(millis)));
        }
    }

    protected String getTimerLabel(long millis) {
        String minuntes = String.format("%" + 2 + "s", (int)millis/1000/60).replace(" ", "0");
        String seconds = String.format("%" + 2 + "s", (int)millis/1000 - ((int)millis/1000/60)*60).replace(" ", "0");

        return minuntes + ":" + seconds;
    }

    protected void timeOut(CountDownTimer timer) {
        if (timer == whiteTimer) {
            if (whiteTimerActive) {
                winner = BLACK;
                setImageViewsArrayNonClickable();
                gameInfoManipulator.setGameInfoToWon(playerBlackName);
                gameInfoManipulator.setGameInfoToBlack();
                activateButtons();
                addScoresToDbIfWon(playerBlackName, playerWhiteName, counterBlackTurns);
            }
        } else {
            if(blackTimerActive) {
                winner = WHITE;
                setImageViewsArrayNonClickable();
                gameInfoManipulator.setGameInfoToWon(playerWhiteName);
                gameInfoManipulator.setGameInfoToWhite();
                activateButtons();
                addScoresToDbIfWon(playerWhiteName, playerBlackName, counterWhiteTurns);
            }
        }
    }
}