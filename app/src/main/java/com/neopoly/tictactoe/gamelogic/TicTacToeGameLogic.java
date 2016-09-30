package com.neopoly.tictactoe.gamelogic;

/**
 * This game logic class manages a TicTacToe-game by dividing the underlying 3x3-matrix into a set
 * of eight 'win lines' in which a gamer could reach the goal of flagging a whole line. Each line
 * additionally holds the sum of all its three fields and the count of filled fields for a faster
 * detection of completed 'win lines' and no more 'winable lines' - neither first nor second gamer
 * can win such a line because both have already flagged fields in it.
 */
public class TicTacToeGameLogic extends AbstractTicTacToeGameLogic {

    private static final int POSSIBLE_WIN_LINES_COUNT = 8;

    private static final int INDEX_SUM_OF_FIELD_VALUES = 3;
    private static final int INDEX_COUNT_FILLED_FIELDS = 4;

    private int[] mRowHead;
    private int[] mRowMiddle;
    private int[] mRowBottom;
    private int[] mColumnLeft;
    private int[] mColumnMiddle;
    private int[] mColumnRight;
    private int[] mDiagonalInc;
    private int[] mDiagonalDec;

    private int mFilledFieldsCount;
    private int mNextGamer;
    private boolean[] mWinableLines;
    private int mWinableLinesCount;
    private int mWinner;
    private boolean[] mWinLines;

    public TicTacToeGameLogic() {
        startNewGame();
    }

    @Override
    public void startNewGame() {

        mRowHead =      new int[]{0, 0, 0, 0, 0};
        mRowMiddle =    new int[]{0, 0, 0, 0, 0};
        mRowBottom =    new int[]{0, 0, 0, 0, 0};
        mColumnLeft =   new int[]{0, 0, 0, 0, 0};
        mColumnMiddle = new int[]{0, 0, 0, 0, 0};
        mColumnRight =  new int[]{0, 0, 0, 0, 0};
        mDiagonalInc =  new int[]{0, 0, 0, 0, 0};
        mDiagonalDec =  new int[]{0, 0, 0, 0, 0};

        mFilledFieldsCount = 0;
        mNextGamer = FIRST_GAMER;

        mWinableLines = new boolean[] {true, true, true, true, true, true, true, true};
        mWinableLinesCount = POSSIBLE_WIN_LINES_COUNT;
        mWinLines = new boolean[] {false, false, false, false, false, false, false, false};
        mWinner = 0;
    }

    @Override
    public int setSignToField(int field) throws IllegalFieldException, GameOverException {
        int roundResult = 0;
        // -2 :: double win of first gamer
        // -1 :: normal win of first gamer
        //  0 :: no winner for now (game not over)
        // +1 :: normal win of second gamer
        // +2 :: double win of second gamer
        // GAME_OVER :: no winner possible any more

        if (mWinner == 0) {
            switch (field) {
                case 0 :                                                            //  0 | - | -
                    if (mRowHead[0] == 0) {                                         // ---|---|---
                        mRowHead[0] = mNextGamer;                                   //  | | \ |
                        mColumnLeft[0] = mNextGamer;                                // ---|---|---
                        mDiagonalDec[0] = mNextGamer;                               //  | |   | \

                        mRowHead[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnLeft[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mDiagonalDec[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowHead[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnLeft[INDEX_COUNT_FILLED_FIELDS]++;
                        mDiagonalDec[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_HEAD);
                        roundResult += checkLineForWin(COLUMN_LEFT);
                        roundResult += checkLineForWin(DIAGONAL_DECREASING);

                    } else throw new IllegalFieldException();
                    break;

                case 1 :                                                            //  - | 1 | -
                    if (mRowHead[1] == 0) {                                         // ---|---|---
                        mRowHead[1] = mNextGamer;                                   //    | | |
                        mColumnMiddle[0] = mNextGamer;                              // ---|---|---
                                                                                    //    | | |
                        mRowHead[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnMiddle[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowHead[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnMiddle[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_HEAD);
                        roundResult += checkLineForWin(COLUMN_MIDDLE);

                    } else throw new IllegalFieldException();
                    break;

                case 2 :                                                            //  - | - | 2
                    if (mRowHead[2] == 0) {                                         // ---|---|---
                        mRowHead[2] = mNextGamer;                                   //    | / | |
                        mColumnRight[0] = mNextGamer;                               // ---|---|---
                        mDiagonalInc[2] = mNextGamer;                               //  / |   | |

                        mRowHead[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnRight[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mDiagonalInc[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowHead[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnRight[INDEX_COUNT_FILLED_FIELDS]++;
                        mDiagonalInc[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_HEAD);
                        roundResult += checkLineForWin(COLUMN_RIGHT);
                        roundResult += checkLineForWin(DIAGONAL_INCREASING);

                    } else throw new IllegalFieldException();
                    break;

                case 3 :                                                            //  | |   |
                    if (mRowMiddle[0] == 0) {                                       // ---|---|---
                        mRowMiddle[0] = mNextGamer;                                 //  3 | - | -
                        mColumnLeft[1] = mNextGamer;                                // ---|---|---
                                                                                    //  | |   |
                        mRowMiddle[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnLeft[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowMiddle[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnLeft[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_MIDDLE);
                        roundResult += checkLineForWin(COLUMN_LEFT);

                    } else throw new IllegalFieldException();
                    break;

                case 4 :                                                            //  \ | | | /
                    if (mRowMiddle[1] == 0) {                                       // ---|---|---
                        mRowMiddle[1] = mNextGamer;                                 //  - | 4 | -
                        mColumnMiddle[1] = mNextGamer;                              // ---|---|---
                        mDiagonalInc[1] = mNextGamer;                               //  / | | | \
                        mDiagonalDec[1] = mNextGamer;

                        mRowMiddle[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnMiddle[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mDiagonalInc[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mDiagonalDec[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowMiddle[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnMiddle[INDEX_COUNT_FILLED_FIELDS]++;
                        mDiagonalInc[INDEX_COUNT_FILLED_FIELDS]++;
                        mDiagonalDec[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_MIDDLE);
                        roundResult += checkLineForWin(COLUMN_MIDDLE);
                        roundResult += checkLineForWin(DIAGONAL_INCREASING);
                        roundResult += checkLineForWin(DIAGONAL_DECREASING);

                    } else throw new IllegalFieldException();
                    break;

                case 5 :                                                            //    |   | |
                    if (mRowMiddle[2] == 0) {                                       // ---|---|---
                        mRowMiddle[2] = mNextGamer;                                 //  - | - | 5
                        mColumnRight[1] = mNextGamer;                               // ---|---|---
                                                                                    //    |   | |
                        mRowMiddle[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnRight[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowMiddle[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnRight[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_MIDDLE);
                        roundResult += checkLineForWin(COLUMN_RIGHT);

                    } else throw new IllegalFieldException();
                    break;

                case 6 :                                                            //  | |   | /
                    if (mRowBottom[0] == 0) {                                       // ---|---|---
                        mRowBottom[0] = mNextGamer;                                 //  | | / |
                        mColumnLeft[2] = mNextGamer;                                // ---|---|---
                        mDiagonalInc[0] = mNextGamer;                               //  6 | - | -

                        mRowBottom[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnLeft[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mDiagonalInc[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowBottom[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnLeft[INDEX_COUNT_FILLED_FIELDS]++;
                        mDiagonalInc[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_BOTTOM);
                        roundResult += checkLineForWin(COLUMN_LEFT);
                        roundResult += checkLineForWin(DIAGONAL_INCREASING);

                    } else throw new IllegalFieldException();
                    break;

                case 7 :                                                            //    | | |
                    if (mRowBottom[1] == 0) {                                       // ---|---|---
                        mRowBottom[1] = mNextGamer;                                 //    | | |
                        mColumnMiddle[2] = mNextGamer;                              // ---|---|---
                                                                                    //  - | 7 | -
                        mRowBottom[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnMiddle[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowBottom[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnMiddle[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_BOTTOM);
                        roundResult += checkLineForWin(COLUMN_MIDDLE);

                    } else throw new IllegalFieldException();
                    break;

                case 8 :                                                            //  \ |   | |
                    if (mRowBottom[2] == 0) {                                       // ---|---|---
                        mRowBottom[2] = mNextGamer;                                 //    | \ | |
                        mColumnRight[2] = mNextGamer;                               // ---|---|---
                        mDiagonalDec[2] = mNextGamer;                               //  - | - | 8

                        mRowBottom[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mColumnRight[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;
                        mDiagonalDec[INDEX_SUM_OF_FIELD_VALUES] += mNextGamer;

                        mRowBottom[INDEX_COUNT_FILLED_FIELDS]++;
                        mColumnRight[INDEX_COUNT_FILLED_FIELDS]++;
                        mDiagonalDec[INDEX_COUNT_FILLED_FIELDS]++;

                        roundResult += checkLineForWin(ROW_BOTTOM);
                        roundResult += checkLineForWin(COLUMN_RIGHT);
                        roundResult += checkLineForWin(DIAGONAL_DECREASING);

                    } else throw new IllegalFieldException();
                    break;

                default:
                    throw new IllegalFieldException();
            }

        } else throw new GameOverException();

        mWinner = roundResult;
        if (mWinner == 0) {
            mNextGamer = -(mNextGamer);
        }
        return roundResult;
    }

    private int checkLineForWin(int line) throws IllegalArgumentException {
        int result = 0;
        int[] checkedLine = null;
        switch (line) {
            case ROW_HEAD:
                checkedLine = mRowHead;
                break;
            case ROW_MIDDLE:
                checkedLine = mRowHead;
                break;
            case ROW_BOTTOM:
                checkedLine = mRowHead;
                break;
            case COLUMN_LEFT:
                checkedLine = mRowHead;
                break;
            case COLUMN_MIDDLE:
                checkedLine = mRowHead;
                break;
            case COLUMN_RIGHT:
                checkedLine = mRowHead;
                break;
            case DIAGONAL_INCREASING:
                checkedLine = mRowHead;
                break;
            case DIAGONAL_DECREASING:
                checkedLine = mRowHead;
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (checkedLine != null && mWinableLines[line]) {
            switch (checkedLine[INDEX_COUNT_FILLED_FIELDS]) {
                case 1:
                    switch (checkedLine[INDEX_SUM_OF_FIELD_VALUES]) {
                        case -1:
                            // DO NOTHING HERE
                            break;
                        case 1:
                            // DO NOTHING HERE
                    }
                    break;
                case 2:
                    switch (checkedLine[INDEX_SUM_OF_FIELD_VALUES]) {
                        case -2:
                            // DO NOTHING HERE
                            break;
                        case 0:
                            mWinableLines[line] = false;
                            mWinableLinesCount--;
                            break;
                        case 2:
                            // DO NOTHING HERE
                    }
                    break;
                case 3:
                    switch (checkedLine[INDEX_SUM_OF_FIELD_VALUES]) {
                        case -3:
                            result = FIRST_GAMER;
                            mWinLines[line] = true;
                            break;
                        case -1:
                            mWinableLines[line] = false;
                            mWinableLinesCount--;
                            break;
                        case 1:
                            mWinableLines[line] = false;
                            mWinableLinesCount--;
                            break;
                        case 3:
                            result = SECOND_GAMER;
                            mWinLines[line] = true;
                    }
            }
        }
        return (result == 0 && mWinableLinesCount == 0)? GAME_OVER : result;
    }

    @Override
    public int[] getWinLines() {
        int[] winLines = null;
        if (mWinner != 0 && mWinner != GAME_OVER) {
            int indexInResultArray = 0;
            int numberOfWins = mWinner / mNextGamer;
            winLines = new int[] {numberOfWins};
            for (int line = 0; line < POSSIBLE_WIN_LINES_COUNT; line++) {
                if (mWinLines[line]) {
                    winLines[indexInResultArray] = line;
                    indexInResultArray++;
                }
            }
        }
        return winLines;
    }

    @Override
    public int getCurrentRound() {
        return mFilledFieldsCount + 1;
    }

    @Override
    public int[] getGameState() {
        return new int[] {
                mRowHead[0],   mRowHead[1],   mRowHead[2],
                mRowMiddle[0], mRowMiddle[1], mRowMiddle[2],
                mRowBottom[0], mRowBottom[1], mRowBottom[2]};
    }
}
