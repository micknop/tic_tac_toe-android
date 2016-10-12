package com.neopoly.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.neopoly.tictactoe.gamelogic.FieldFlag;
import com.neopoly.tictactoe.gamelogic.GameState;
import com.neopoly.tictactoe.gamelogic.TicTacToeGameLogic;

public class PlayingFieldActivity extends Activity {

    private static final String X = "X";
    private static final String O = "O";

    private TicTacToeGameLogic mGameLogic;
    private GameState mGameState;
    private boolean mRoundStarted;
    private int mGameRound;

    private FieldFlag mNextTurn;
    private boolean mFirstX;

    private String mNameGamerX;
    private String mNameGamerO;

    private int mWinsX;
    private int mWinsO;

    private ImageButton[] mButtonPlayingField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playing_field);

        mGameLogic = new TicTacToeGameLogic();

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNameGamerX = extras.getString(MainActivity.NAME_X);
            mNameGamerO = extras.getString(MainActivity.NAME_O);
            ((TextView) findViewById(R.id.tv_gamer_x)).setText(mNameGamerX);
            ((TextView) findViewById(R.id.tv_gamer_o)).setText(mNameGamerO);
            mGameRound = extras.getInt(MainActivity.GAME_ROUND);
            mWinsX = extras.getInt(MainActivity.WINS_X);
            mWinsO = extras.getInt(MainActivity.WINS_O);
            mFirstX = !extras.getBoolean(MainActivity.FIRST_X);
        }

        updateDisplayedScores();

        mButtonPlayingField = new ImageButton[9];
        mButtonPlayingField[0] = (ImageButton) findViewById(R.id.ib_field0);
        mButtonPlayingField[1] = (ImageButton) findViewById(R.id.ib_field1);
        mButtonPlayingField[2] = (ImageButton) findViewById(R.id.ib_field2);
        mButtonPlayingField[3] = (ImageButton) findViewById(R.id.ib_field3);
        mButtonPlayingField[4] = (ImageButton) findViewById(R.id.ib_field4);
        mButtonPlayingField[5] = (ImageButton) findViewById(R.id.ib_field5);
        mButtonPlayingField[6] = (ImageButton) findViewById(R.id.ib_field6);
        mButtonPlayingField[7] = (ImageButton) findViewById(R.id.ib_field7);
        mButtonPlayingField[8] = (ImageButton) findViewById(R.id.ib_field8);

        clearPlayingField();
    }

    private void clearPlayingField() {
        for (ImageButton field : mButtonPlayingField) {
            field.setClickable(true);
            field.setElevation(20);
            field.setImageResource(R.drawable.ic_empty);
        }
        mGameRound++;
        mRoundStarted = false;
        determineFirstGamer();

        mGameLogic.startNewGame();
        mGameState = GameState.OPEN;

        String startText = String.format(getString(R.string.txt_start_round), mGameRound);
        ((TextView) findViewById(R.id.tv_start_x)).setText(startText);
        ((TextView) findViewById(R.id.tv_start_o)).setText(startText);

        showStartRoundField();
    }

    private boolean determineFirstGamer() {
        if (!mRoundStarted) {
            if (mWinsX == mWinsO) {
                mFirstX = !mFirstX;
            } else {
                mFirstX = mWinsX < mWinsO;
            }
            mNextTurn = FieldFlag.FIRST_GAMERS_FLAG;
        }
        return mFirstX;
    }

    private void deactivateAllFields() {
        for (ImageButton field : mButtonPlayingField) {
            field.setClickable(false);
        }
    }

    private void updateDisplayedScores() {
        ((TextView) findViewById(R.id.tv_score_x)).setText(mWinsX + " : " + mWinsO);
        ((TextView) findViewById(R.id.tv_score_o)).setText(mWinsO + " : " + mWinsX);
    }

    private void checkGameState() {

        if (mRoundStarted && !mGameState.equals(GameState.OPEN)) {

            deactivateAllFields();

            String winnersName;
            Toast toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);

            switch (mGameState) {

                case WINNER_FIRST:

                    // Update the game-score
                    if (mFirstX) {
                        mWinsX++;
                        winnersName = mNameGamerX + X;
                    } else {
                        mWinsO++;
                        winnersName = mNameGamerO + O;
                    }
                    updateDisplayedScores();

                    toast.setText(winnersName + getString(R.string.txt_toast_suffix_win));
                    toast.show();
                    break;

                case WINNER_SECOND:

                    // Update the game-score
                    if (mFirstX) {
                        mWinsO++;
                        winnersName = mNameGamerO + O;
                    } else {
                        mWinsX++;
                        winnersName = mNameGamerX + X;
                    }
                    updateDisplayedScores();

                    toast.setText(winnersName + getString(R.string.txt_toast_suffix_win));
                    toast.show();
                    break;

                case DOUBLE_WIN_FIRST:

                    // Update the game-score
                    if (mFirstX) {
                        mWinsX = mWinsX + 2;
                        winnersName = mNameGamerX + X;
                    } else {
                        mWinsO = mWinsO + 2;
                        winnersName = mNameGamerO + O;
                    }
                    updateDisplayedScores();

                    toast.setText(winnersName + getString(R.string.txt_toast_suffix_double_win));
                    toast.show();
                    break;

                case GAME_OVER:

                    toast.setText(getString(R.string.txt_toast_game_over));
                    toast.show();
            }

            clearPlayingField();
        }
    }

    public void onFieldClick(View view) {

        if (mGameState.equals(GameState.OPEN)) {

            mRoundStarted = true;

            ImageButton clickedField = (ImageButton) view;
            switch (mNextTurn) {
                case FIRST_GAMERS_FLAG:
                    if  (mFirstX) {
                        clickedField.setImageResource(R.drawable.ic_flag_x);
                        findViewById(R.id.display_gamer_x).setBackgroundColor(Color.parseColor("#4e342e"));
                        findViewById(R.id.display_gamer_o).setBackgroundColor(Color.parseColor("#ffcc80"));
                    } else {
                        clickedField.setImageResource(R.drawable.ic_flag_o);
                        findViewById(R.id.display_gamer_x).setBackgroundColor(Color.parseColor("#ffcc80"));
                        findViewById(R.id.display_gamer_o).setBackgroundColor(Color.parseColor("#4e342e"));
                    }
                    mNextTurn = FieldFlag.SECOND_GAMERS_FLAG;
                    break;
                case SECOND_GAMERS_FLAG:
                    if  (mFirstX) {
                        clickedField.setImageResource(R.drawable.ic_flag_o);
                        findViewById(R.id.display_gamer_x).setBackgroundColor(Color.parseColor("#ffcc80"));
                        findViewById(R.id.display_gamer_o).setBackgroundColor(Color.parseColor("#4e342e"));
                    } else {
                        clickedField.setImageResource(R.drawable.ic_flag_x);
                        findViewById(R.id.display_gamer_x).setBackgroundColor(Color.parseColor("#4e342e"));
                        findViewById(R.id.display_gamer_o).setBackgroundColor(Color.parseColor("#ffcc80"));
                    }
                    mNextTurn = FieldFlag.FIRST_GAMERS_FLAG;
            }

            clickedField.setElevation(50);
            clickedField.setClickable(false);

            switch (view.getId()) {
                case R.id.ib_field0:
                    mGameState = mGameLogic.setFlagToField(0);
                    break;
                case R.id.ib_field1:
                    mGameState = mGameLogic.setFlagToField(1);
                    break;
                case R.id.ib_field2:
                    mGameState = mGameLogic.setFlagToField(2);
                    break;
                case R.id.ib_field3:
                    mGameState = mGameLogic.setFlagToField(3);
                    break;
                case R.id.ib_field4:
                    mGameState = mGameLogic.setFlagToField(4);
                    break;
                case R.id.ib_field5:
                    mGameState = mGameLogic.setFlagToField(5);
                    break;
                case R.id.ib_field6:
                    mGameState = mGameLogic.setFlagToField(6);
                    break;
                case R.id.ib_field7:
                    mGameState = mGameLogic.setFlagToField(7);
                    break;
                case R.id.ib_field8:
                    mGameState = mGameLogic.setFlagToField(8);
            }

            checkGameState();
        }
    }

    private void showStartRoundField() {
        findViewById(R.id.start).setVisibility(View.VISIBLE);
        if (mFirstX) {
            findViewById(R.id.tv_start_x).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_start_o).setVisibility(View.INVISIBLE);
            findViewById(R.id.display_gamer_x).setBackgroundColor(Color.parseColor("#ffcc80"));
            findViewById(R.id.display_gamer_o).setBackgroundColor(Color.parseColor("#4e342e"));
        } else {
            findViewById(R.id.tv_start_x).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_start_o).setVisibility(View.VISIBLE);
            findViewById(R.id.display_gamer_x).setBackgroundColor(Color.parseColor("#4e342e"));
            findViewById(R.id.display_gamer_o).setBackgroundColor(Color.parseColor("#ffcc80"));
        }
    }

    public void onStartClick(View view) {

        findViewById(R.id.start).setVisibility(View.INVISIBLE);
    }

    private void endGame() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.NAME_X, mNameGamerX);
        resultIntent.putExtra(MainActivity.NAME_O, mNameGamerO);
        resultIntent.putExtra(MainActivity.WINS_X, mWinsX);
        resultIntent.putExtra(MainActivity.WINS_O, mWinsO);
        resultIntent.putExtra(MainActivity.FIRST_X, mFirstX);
        resultIntent.putExtra(MainActivity.GAME_ROUND, mRoundStarted ? mGameRound : (mGameRound - 1));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mRoundStarted) {

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.txt_title_quit_round))
                        .setMessage(getString(R.string.txt_message_quit_round))
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlayingFieldActivity.this.endGame();
                            }
                        }).show();
            } else {
                this.endGame();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
