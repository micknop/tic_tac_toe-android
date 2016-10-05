package com.neopoly.tictactoe.gamelogic;

/**
 * This enum type bundles all lines in a TicTacToe-game in which a gamer could potentially
 * reach the goal of filling one of them completely with his flags to get a whole 'win line'.
 * Besides each line specifies it's location in the playing field consisting of nine fields.
 */
public enum GameLine {
    ROW_HEAD(
            new int[] { 0,  1,  2,      // 0|1|2
                       -1, -1, -1,      // -|-|-
                       -1, -1, -1}),    // -|-|-
    ROW_MIDDLE(
            new int[] {-1, -1, -1,      // -|-|-
                        0,  1,  2,      // 0|1|2
                       -1, -1, -1}),    // -|-|-
    ROW_BOTTOM(
            new int[] {-1, -1, -1,      // -|-|-
                       -1, -1, -1,      // -|-|-
                        0,  1,  2}),    // 0|1|2
    COLUMN_LEFT(
            new int[] { 0, -1, -1,      // 0|-|-
                        1, -1, -1,      // 1|-|-
                        2, -1, -1}),    // 2|-|-
    COLUMN_MIDDLE(
            new int[] {-1,  0, -1,      // -|0|-
                       -1,  1, -1,      // -|1|-
                       -1,  2, -1}),    // -|2|-
    COLUMN_RIGHT(
            new int[] {-1, -1,  0,      // -|-|0
                       -1, -1,  1,      // -|-|1
                       -1, -1,  2}),    // -|-|2
    DIAGONAL_INCREASING(
            new int[] {-1, -1,  2,      // -|-|2
                       -1,  1, -1,      // -|1|-
                        0, -1, -1}),    // 0|-|-
    DIAGONAL_DECREASING(
            new int[] { 0, -1, -1,      // 0|-|-
                       -1,  1, -1,      // -|1|-
                       -1, -1,  2});    // -|-|2

    public static final int GAME_LINES_COUNT = 8;

    private final int[] mLocation;      // The location in the playing field marked by indices 0, 1, 2

    GameLine(int[] pLocation) {
        mLocation = pLocation;
    }

    int[] getLocation() {
        return mLocation;
    }

    int getIndexForField(int pFieldIndex) {
        return mLocation[pFieldIndex];
    }
}
