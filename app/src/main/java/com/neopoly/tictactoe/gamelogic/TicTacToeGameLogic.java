package com.neopoly.tictactoe.gamelogic;

/**
 * This game logic class manages a TicTacToe-game by dividing the underlying 3x3-matrix into a set
 * of eight 'win lines' in which a gamer could reach the goal of flagging a whole line. Each line
 * additionally holds the sum of all its three fields and the count of filled fields for a faster
 * detection of completed 'win lines' and no more 'winable lines' - neither first nor second gamer
 * can win such a line because both have already flagged fields in it.
 */
public class TicTacToeGameLogic extends AbstractTicTacToeGameLogic {

    /** The algebraic sign to declare the first gamer as field owner or game winner. */
    private static final int FIRST_GAMER = -1;
    /** The algebraic sign to declare the second gamer as field owner or game winner. */
    private static final int SECOND_GAMER = 1;
    /** The field value to describe an empty field. */
    private static final int EMPTY_FIELD = 0;

    /** The index of the internal integer array holding a line's sum of all its three fields. */
    private static final int INDEX_SUM_OF_FIELD_VALUES = 3;
    /** The index of the internal integer array holding a line's count of already filled fields. */
    private static final int INDEX_COUNT_FILLED_FIELDS = 4;

    /**
     * This two-dimensional integer array keeps one simple integer array entry per game line:
     * int[lineIndex] = {firstFieldValue,       // 0
     *                   secondFieldValue,      // 1
     *                   thirdFieldValue,       // 2
     *                   sumOfFlagValues,       // 3 [INDEX_SUM_OF_FIELD_VALUES]
     *                   countOfFilledFields}   // 4 [INDEX_COUNT_FILLED_FIELDS]
     * These eight integer arrays describe the whole state of the playing field. Each of them holds
     * the values of three related fields of a possible 'win line' - redundant with crossing 'win
     * lines' for a more simple analysis of the current game situation (open game, game winner and
     * impossible to win detection). To save calculation processes it additionally holds two integer
     * values as metadata - the first [at INDEX_SUM_OF_FIELD_VALUES] stores the sum of all three
     * field values and the second [at INDEX_COUNT_FILLED_FIELDS] counts the number of selected
     * fields of this line.
     */
    private int[][] mGameLines;
    /**
     * This helper array is initialized with 'false' for all eight lines at each start of a new game
     * and it marks the 'win lines' - one or two in number - reached in the last turn by the winner.
     */
    private boolean[] mWinLines;
    /**
     * This helper array marks the lines which contain no mixed flags of both gamers and so could
     * be won of at least one of them. It is initialized with 'true' for all eight lines at start.
     */
    private boolean[] mWinableLines;
    /** The count of indices marked with 'true' in the helper array 'mWinableLines'. */
    private int mWinableLinesCount;
    /** State of the current game round as returned by the last 'setFlagToField(field)'-call. */
    private GameState mGameState;
    /**
     * Switching between the constant of the first [FIRST_GAMER] and second gamer [SECOND_GAMER],
     * initialized with 'FIRST_GAMER', switching with a valid turn (to 'SECOND_GAMER' or back)
     * and in this way the variable always describes the gamer having the next field selection.
     */
    private int mNextGamer;

    /** A representation of the whole current playing field state using 'FieldFlag's. */
    private FieldFlag[] mPlayingField;

    public TicTacToeGameLogic() {

        // Start a new game and let initialize the game state
        // while creating a new object of this TicTacToe-game.
        startNewGame();
    }

    @Override
    public void startNewGame() {

        // Initialize all member variables to start a new game.

        mGameLines = new int[GameLine.GAME_LINES_COUNT][];
        mWinLines = new boolean[GameLine.GAME_LINES_COUNT];
        mWinableLines = new boolean[GameLine.GAME_LINES_COUNT];

        for (GameLine line : GameLine.values()) {
            mGameLines[line.ordinal()] = new int[]{EMPTY_FIELD, EMPTY_FIELD, EMPTY_FIELD, 0, 0};
            mWinLines[line.ordinal()] = false;
            mWinableLines[line.ordinal()] = true;
        }
        mWinableLinesCount = GameLine.GAME_LINES_COUNT;
        mGameState = GameState.OPEN;
        mNextGamer = FIRST_GAMER;

        mPlayingField = new FieldFlag[TOTAL_FIELD_COUNT];
        for (int field = 0; field < TOTAL_FIELD_COUNT; field++) {
            mPlayingField[field] = FieldFlag.EMPTY_FIELD;
        }
    }

    @Override
    public GameState setFlagToField(int field) {

        // Only set the flag to the field if...
        if (mGameState.equals(GameState.OPEN)                             // ...the game is open
                && field >= 0 && field < TOTAL_FIELD_COUNT                // ...the index is valid
                && mPlayingField[field].equals(FieldFlag.EMPTY_FIELD)) {  // ...and field is empty.

            // Set the flag of the current gamer to the indexed field of whole the playing field.
            switch (mNextGamer) {
                case FIRST_GAMER:
                    mPlayingField[field] = FieldFlag.FIRST_GAMERS_FLAG;
                    break;
                case SECOND_GAMER:
                    mPlayingField[field] = FieldFlag.SECOND_GAMERS_FLAG;
            }

            // Find all influenced game lines and let set the flag to their related index.
            int indexInLine;
            for (GameLine line : GameLine.values()) {
                indexInLine = line.getIndexForField(field);
                if (indexInLine != -1) {
                    setFlagToFieldInLine(line.ordinal(), indexInLine);
                }
            }

            if (mGameState.equals(GameState.OPEN)) {
                // The variable 'mNextGamer' is only shifted if there is a next game round.
                mNextGamer = -(mNextGamer);
            } // Otherwise 'mNextGamer' holds the winner or last manipulator of ended games.
        }

        return mGameState;
    }

    /**
     * This helper method sets the flag of the current gamer to the received line at the declared
     * index and updates the included metadata ('sum of field values' and 'filled fields count')
     * as well as the 'win' possibilities associated with this current line.
     *
     * @param line the index of the influenced game line.
     * @param fieldIndexInLine the index of the selected field in the game line.
     */
    private void setFlagToFieldInLine(int line, int fieldIndexInLine) {

        // Pick the required line up
        int[] gameLine = mGameLines[line];

        // Flag the field in line and
        // Update the 'sum of field values'
        // as well as the 'filled fields count'
        gameLine[fieldIndexInLine] = mNextGamer;
        gameLine[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
        gameLine[INDEX_COUNT_FILLED_FIELDS]++;

        // If the current line has been a possible 'win line' until now, this status must be updated:
        if (mWinableLines[line]) {                                          //-----||-----|-----|-----||
            if (gameLine[INDEX_SUM_OF_FIELD_VALUES] == (mNextGamer * 3)) {  //-----||  FILLED FIELDS  ||
                mWinLines[line] = true;                                     //-----||-----|-----|-----||
                if (mNextGamer == FIRST_GAMER) {                            // SUM ||  1  |  2  |  3  ||
                    if (mGameState.equals(GameState.WINNER_FIRST)) {        //-----||-----|-----|-----||
                        mGameState = GameState.DOUBLE_WIN_FIRST;            //  -3  |  X  |  X  | WIN ||
                    } else {                                                //-----||-----|-----|-----||
                        mGameState = GameState.WINNER_FIRST;                //  -2  |  X  | NOT |  X  ||
                    }                                                       //-----||-----|-----|-----||
                } else {                                                    //  -1  | NOT |  X  | MIX ||
                    mGameState = GameState.WINNER_SECOND;                   //-----||-----|-----|-----||
                }                                                           //   0  |  X  | MIX |  X  ||---------------------------------
            } else if (gameLine[INDEX_COUNT_FILLED_FIELDS] == 3) {          //-----||-----|-----|-----||  legend:                       |
                mWinableLines[line] = false;                                //  +1  | NOT |  X  | MIX ||---------------------------------
                mWinableLinesCount--;                                       //-----||-----|-----|-----||   X  :: impossible combination |
            } else if (gameLine[INDEX_COUNT_FILLED_FIELDS] == 2             //  +2  |  X  | NOT |  X  ||  MIX ::  mixed flags in line   |
                    && gameLine[INDEX_SUM_OF_FIELD_VALUES] == 0) {          //-----||-----|-----|-----||  NOT ::  gamer could win line  |
                mWinableLines[line] = false;                                //  +3  |  X  |  X  | WIN ||  WIN ::  gamer wins this line  |
                mWinableLinesCount--;                                       //-----||-----|-----|-----||---------------------------------
            }
        }

        if (mGameState.equals(GameState.OPEN) && mWinableLinesCount == 0) {
            mGameState = GameState.GAME_OVER;
        }
    }

    @Override
    public GameLine[] getWinLines() {

        GameLine[] winLines = null;
        int numberOfWins = 0;

        // Only if there is still a winner of the current game...
        switch (mGameState) {
            case DOUBLE_WIN_FIRST:                  // There are two 'GameLine's as 'win lines'
                numberOfWins++;
            case WINNER_FIRST: case WINNER_SECOND:  // There is one 'GameLine' as 'win line'
                numberOfWins++;

                winLines = new GameLine[numberOfWins];
                int indexInResultArray = 0;
                // ...collect the 'GameLine's which are 'win lines'.
                for (GameLine line : GameLine.values()) {
                    if (mWinLines[line.ordinal()]) {
                        winLines[indexInResultArray] = line;
                        indexInResultArray++;
                    }
                }
        }

        return winLines;
    }

    @Override
    public FieldFlag[] getPlayingFieldState() {
        return mPlayingField.clone();
    }
}
