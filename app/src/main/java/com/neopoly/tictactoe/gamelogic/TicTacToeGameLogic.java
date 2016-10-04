package com.neopoly.tictactoe.gamelogic;

/**
 * This game logic class manages a TicTacToe-game by dividing the underlying 3x3-matrix into a set
 * of eight 'win lines' in which a gamer could reach the goal of flagging a whole line. Each line
 * additionally holds the sum of all its three fields and the count of filled fields for a faster
 * detection of completed 'win lines' and no more 'winable lines' - neither first nor second gamer
 * can win such a line because both have already flagged fields in it.
 */
public class TicTacToeGameLogic extends AbstractTicTacToeGameLogic {

    /** The number of lines forming the playing field - each line is three fields long. */
    private static final int GAME_LINES_COUNT = 8;

    /** The index of the internal integer array holding a line's sum of all its three fields. */
    private static final int INDEX_SUM_OF_FIELD_VALUES = 3;
    /** The index of the internal integer array holding a line's count of already filled fields. */
    private static final int INDEX_COUNT_FILLED_FIELDS = 4;

    /**
     * This two-dimensional integer array keeps one simple integer array entry per game line:
     * int[lineIndex] = {firstFieldValue,           // 0
     *                   secondFieldValue,          // 1
     *                   thirdFieldValue,           // 2
     *                   sumOfFlagValues,           // 3 [INDEX_SUM_OF_FIELD_VALUES]
     *                   countOfFilledFields}       // 4 [INDEX_COUNT_FILLED_FIELDS]
     * These eight integer arrays describe the whole state of the playing field. Each of them holds
     * the values of three related fields of a possible 'win lines' - redundant with crossing 'win
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
    private int mWinner;
    /**
     * Switching between the constant of the first [FIRST_GAMER] and second gamer [SECOND_GAMER],
     * initialized with 'FIRST_GAMER', switching with a valid turn (to 'SECOND_GAMER' or back)
     * and in this way the variable always describes the gamer having the next field selection.
     */
    private int mNextGamer;

    public TicTacToeGameLogic() {

        // Start a new game and let initialize the game state
        // while creating a new object of this TicTacToe-game.
        startNewGame();
    }

    @Override
    public void startNewGame() {

        // Initialize all member variables to start a new game.

        mGameLines = new int[GAME_LINES_COUNT][];
        mWinLines = new boolean[GAME_LINES_COUNT];
        mWinableLines = new boolean[GAME_LINES_COUNT];

        for (int line = 0; line < GAME_LINES_COUNT; line++) {
            mGameLines[line] = new int[]{0, 0, 0, 0, 0};
            mWinLines[line] = false;
            mWinableLines[line] = true;
        }
        mWinableLinesCount = GAME_LINES_COUNT;
        mWinner = 0;
        mNextGamer = FIRST_GAMER;
    }

    @Override
    /*  The possible return values of the
     * 'setFlagToField(field)'-method are:
     *
     *  0 :: no winner for now (game not over)
     * -1 :: normal win of first gamer
     * +1 :: normal win of second gamer
     * -2 :: double win of first gamer
     * +2 :: double win of second gamer
     * GAME_OVER :: no winner possible anymore
     */
    public int setFlagToField(int field) throws IllegalFieldException, GameOverException {

        int result = 0;

        // Only set the flag to a field if the game is not already ended
        if (mWinner == 0) {

            switch (field) {

                case 0 :   // field-index '0' influences the following three lines:    0 | - | -
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_HEAD, 0);                 //    | | \ |
                    result += setFlagToFieldInLine(COLUMN_LEFT, 0);              //   ---|---|---
                    result += setFlagToFieldInLine(DIAGONAL_DECREASING, 0);      //    | |   | \
                    break;

                case 1 :   // field-index '1' influences the following two lines:      - | 1 | -
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_HEAD, 1);                 //      | | |
                    result += setFlagToFieldInLine(COLUMN_MIDDLE, 0);            //   ---|---|---
                    break;                                                       //      | | |


                case 2 :   // field-index '2' influences the following three lines:    - | - | 2
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_HEAD, 2);                 //      | / | |
                    result += setFlagToFieldInLine(COLUMN_RIGHT, 0);             //   ---|---|---
                    result += setFlagToFieldInLine(DIAGONAL_INCREASING, 2);      //    / |   | |
                    break;

                case 3 :   // field-index '3' influences the following two lines:      | |   |
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_MIDDLE, 0);               //    3 | - | -
                    result += setFlagToFieldInLine(COLUMN_LEFT, 1);              //   ---|---|---
                    break;                                                       //    | |   |


                case 4 :   // field-index '4' influences the following four lines:     \ | | | /
                    result += setFlagToFieldInLine(ROW_MIDDLE, 1);               //   ---|---|---
                    result += setFlagToFieldInLine(COLUMN_MIDDLE, 1);            //    - | 4 | -
                    result += setFlagToFieldInLine(DIAGONAL_INCREASING, 1);      //   ---|---|---
                    result += setFlagToFieldInLine(DIAGONAL_DECREASING, 1);      //    / | | | \
                    break;

                case 5 :   // field-index '5' influences the following two lines:        |   | |
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_MIDDLE, 2);               //    - | - | 5
                    result += setFlagToFieldInLine(COLUMN_RIGHT, 1);             //   ---|---|---
                    break;                                                       //      |   | |


                case 6 :   // field-index '6' influences the following three lines:    | |   | /
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_BOTTOM, 0);               //    | | / |
                    result += setFlagToFieldInLine(COLUMN_LEFT, 2);              //   ---|---|---
                    result += setFlagToFieldInLine(DIAGONAL_INCREASING, 0);      //    6 | - | -
                    break;

                case 7 :   // field-index '7' influences the following two lines:        | | |
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_BOTTOM, 1);               //      | | |
                    result += setFlagToFieldInLine(COLUMN_MIDDLE, 2);            //   ---|---|---
                    break;                                                       //    - | 7 | -


                case 8 :   // field-index '8' influences the following three lines:    \ |   | |
                                                                                 //   ---|---|---
                    result += setFlagToFieldInLine(ROW_BOTTOM, 2);               //      | \ | |
                    result += setFlagToFieldInLine(COLUMN_RIGHT, 2);             //   ---|---|---
                    result += setFlagToFieldInLine(DIAGONAL_DECREASING, 2);      //    - | - | 8
                    break;

                default:
                    throw new IllegalFieldException();
            }

        } else throw new GameOverException();

        // If 'GAME_OVER'-value was added multiple times to the result, shrink it to a single one
        if (result < GAME_OVER) {
            result = GAME_OVER;
        }

        if (result == 0) {
            // The member variable 'mNextGamer' is only shifted if there is a next game round.
            mNextGamer = -(mNextGamer);
        }   // Otherwise 'mNextGamer' holds the winner or last manipulator of an ended game.

        mWinner = result;
        return result;
    }

    /**
     * This helper method sets the flag of the current gamer to the received line at the declared
     * index and updates the included metadata ('sum of field values' and 'filled fields count')
     * as well as the 'win' possibilities associated with this current line.
     *
     * @param line the index of this game line as specified in the interface.
     * @param fieldIndexInLine the index of the current field in this line.
     * @return an integer value describing the changes in win possibilities.
     * @throws IllegalFieldException if the declared field is not empty.
     */
    private int setFlagToFieldInLine(int line, int fieldIndexInLine) throws IllegalFieldException {

        int result = 0;

        // Pick the required line up
        int[] gameLine = mGameLines[line];

        // Check if the selected field is really empty
        if (gameLine[fieldIndexInLine] == EMPTY_FIELD) {

            // Flag the field in line and
            // Update the 'sum of field values'
            // as well as the 'filled fields count'
            gameLine[fieldIndexInLine] = mNextGamer;
            gameLine[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
            gameLine[INDEX_COUNT_FILLED_FIELDS]++;

            // If the current line has been a possible 'win line' until now, this status must be updated:
            if (mWinableLines[line]) {                                          //-----||-----|-----|-----||
                                                                                //-----||  FILLED FIELDS  ||
                if (gameLine[INDEX_SUM_OF_FIELD_VALUES] == (mNextGamer * 3)) {  //-----||-----|-----|-----||
                                                                                // SUM ||  1  |  2  |  3  ||
                    result = mNextGamer;                                        //-----||-----|-----|-----||
                    mWinLines[line] = true;                                     //  -3  |  X  |  X  | WIN ||
                                                                                //-----||-----|-----|-----||
                } else if (gameLine[INDEX_COUNT_FILLED_FIELDS] == 3) {          //  -2  |  X  | NOT |  X  ||
                                                                                //-----||-----|-----|-----||
                    mWinableLines[line] = false;                                //  -1  | NOT |  X  | MIX ||
                    mWinableLinesCount--;                                       //-----||-----|-----|-----||
                                                                                //   0  |  X  | MIX |  X  ||---------------------------------
                } else if (gameLine[INDEX_COUNT_FILLED_FIELDS] == 2             //-----||-----|-----|-----||  legend:                       |
                        && gameLine[INDEX_SUM_OF_FIELD_VALUES] == 0) {          //  +1  | NOT |  X  | MIX ||---------------------------------
                                                                                //-----||-----|-----|-----||   X  :: impossible combination |
                    mWinableLines[line] = false;                                //  +2  |  X  | NOT |  X  ||  MIX ::  mixed flags in line   |
                    mWinableLinesCount--;                                       //-----||-----|-----|-----||  NOT ::  gamer could win line  |
                }                                                               //  +3  |  X  |  X  | WIN ||  WIN ::  gamer wins this line  |
            }                                                                   //-----||-----|-----|-----||---------------------------------

        } else throw new IllegalFieldException();

        return (result == 0 && mWinableLinesCount == 0)? GAME_OVER : result;
    }

    @Override
    public int[] getWinLines() {

        int[] winLines = null;

        // Only if there is still a winner of the current game...
        if (mWinner != 0 && mWinner != GAME_OVER) {

            int indexInResultArray = 0;
            int numberOfWins = mWinner / mNextGamer;

            winLines = new int[numberOfWins];

            // ...collect the indices of the reached 'win lines' - which could be one or two.
            for (int line = 0; line < GAME_LINES_COUNT; line++) {

                if (mWinLines[line]) {
                    winLines[indexInResultArray] = line;
                    indexInResultArray++;
                }
            }
        } // ...otherwise return 'null'.
        return winLines;
    }

    @Override
    public int[] getGameState() {
        // As result of the redundant storage of field selection flags in eight game lines,
        // it is sufficient to take only the values of three of these lines (here the rows)
        // to represent the whole state of the current game in an array of nine integers.
        return new int[] {
                mGameLines[0][0], mGameLines[0][1], mGameLines[0][2],   // the values of head row
                mGameLines[1][0], mGameLines[1][1], mGameLines[1][2],   // the values of middle row
                mGameLines[2][0], mGameLines[2][1], mGameLines[2][2]};  // the values of bottom row
    }
}
