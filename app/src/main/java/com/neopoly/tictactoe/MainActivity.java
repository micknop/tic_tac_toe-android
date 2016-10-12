package com.neopoly.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {

    protected final static String NAME_X = "nameX";
    protected final static String NAME_O = "nameO";
    protected final static String GAME_ROUND = "gameRound";
    protected final static String WINS_X = "winsX";
    protected final static String WINS_O = "winsO";
    protected final static String FIRST_X = "firstX";

    private Bundle mCurrentGameScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentGameScore = null;
        findViewById(R.id.current_game).setVisibility(View.INVISIBLE);
    }

    public void onButtonClick(View view) {

        Intent intent;

        switch (view.getId()) {

            case R.id.bt_start:

                intent = new Intent(this, PlayingFieldActivity.class);
                intent.putExtra(NAME_X, ((EditText) findViewById(R.id.et_gamer_x)).getText().toString());
                intent.putExtra(NAME_O, ((EditText) findViewById(R.id.et_gamer_o)).getText().toString());
                intent.putExtra(GAME_ROUND, 0);
                intent.putExtra(WINS_X, 0);
                intent.putExtra(WINS_O, 0);

                boolean isFirstX;
                switch (((RadioGroup) findViewById(R.id.rg_first)).getCheckedRadioButtonId()) {
                    case R.id.rb_x_first:
                        isFirstX = true;
                        break;
                    case R.id.rb_o_first:
                        isFirstX = false;
                        break;
                    default:
                        isFirstX = Math.random() < 0.5;
                }
                intent.putExtra(FIRST_X, isFirstX);

                startActivityForResult(intent, 0);
                break;

            case R.id.bt_continue:

                intent = new Intent(this, PlayingFieldActivity.class);
                intent.putExtras(mCurrentGameScore);
                startActivityForResult(intent, 0);
                break;

            case R.id.bt_discard:

                ((EditText) findViewById(R.id.et_gamer_x))
                        .setText(mCurrentGameScore.getString(MainActivity.NAME_X));
                ((EditText) findViewById(R.id.et_gamer_o))
                        .setText(mCurrentGameScore.getString(MainActivity.NAME_O));

                if (mCurrentGameScore.getBoolean(FIRST_X)) {
                    ((RadioButton) findViewById(R.id.rb_x_first)).setChecked(true);
                } else {
                    ((RadioButton) findViewById(R.id.rb_o_first)).setChecked(true);
                }

                findViewById(R.id.current_game).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        if (resultCode == RESULT_OK && data != null) {

            mCurrentGameScore = data.getExtras();
            if (mCurrentGameScore.getInt(GAME_ROUND) == 0) {
                mCurrentGameScore = null;
                findViewById(R.id.current_game).setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.current_game).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.tv_name_gamer_x))
                        .setText(mCurrentGameScore.getString(NAME_X));
                ((TextView) findViewById(R.id.tv_name_gamer_o))
                        .setText(mCurrentGameScore.getString(NAME_O));
                ((TextView) findViewById(R.id.tv_wins_x))
                        .setText(":   " + mCurrentGameScore.getInt(WINS_X));

                ((TextView) findViewById(R.id.tv_rounds))
                        .setText(String.format(getString(R.string.txt_round_count),
                                Integer.valueOf(mCurrentGameScore.getInt(GAME_ROUND))));
                ((TextView) findViewById(R.id.tv_wins_o))
                        .setText(":   " + mCurrentGameScore.getInt(WINS_O));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && (findViewById(R.id.current_game).getVisibility() == View.VISIBLE)) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.txt_title_quit_game))
                    .setMessage(getString(R.string.txt_message_quit_game))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    }).show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
