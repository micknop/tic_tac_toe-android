package com.neopoly.tictactoe.gamelogic;

/**
 * This interface specifies all methods which each game logic class should provide to manage a
 * TicTacToe-game and support communication with client classes. In a typical TicTacToe-game two
 * gamers select in alternating sequence one empty field in a 3x3-matrix and flag it as their own.
 * The goal is to fill a whole line - means all three fields of a row, column or diagonal - with
 * the own flags before the opponent reach this goal. In some rare cases a gamer can reach this
 * goal for two different lines with his last selection - a 'double win'. In this case the double
 * 'signed' gamer value is returned to offer client classes the possibility to give extra points.
 * As soon as no one of the two gamers can win the current game anymore, the 'GAME_OVER' return
 * value should report this dead-end state to the client classes, so they can start a new game.
 */
public interface TicTacToeGameLogicInterface {

    /** The count of all fields forming the 3x3-matrix of a TicTacToe playing field is '9'. */
    public static final int TOTAL_FIELD_COUNT = 9;

    /** The algebraic sign to declare the first gamer as field owner or game winner. */
    public static final int FIRST_GAMER = -1;
    /** The algebraic sign to declare the second gamer as field owner or game winner. */
    public static final int SECOND_GAMER = 1;
    /** The field value to describe an empty field. */
    public static final int EMPTY_FIELD = 0;

    /** Constant for the character 'X' to customize the printable state of a game. */
    public static final Character CHAR_GAMER_X = 'X';
    /** Constant for the character 'O' to customize the printable state of a game. */
    public static final Character CHAR_GAMER_O = 'O';
    /** Constant describing the used character for empty fields in printable game states. */
    public static final Character CHAR_EMPTY_FIELD = ' ';

    /** Index constant which declares the head row as a possible win line. */
    public static final int ROW_HEAD = 0;
    /** Index constant which declares the middle row as a possible win line. */
    public static final int ROW_MIDDLE = 1;
    /** Index constant which declares the bottom row as a possible win line. */
    public static final int ROW_BOTTOM = 2;
    /** Index constant which declares the left column as a possible win line. */
    public static final int COLUMN_LEFT = 3;
    /** Index constant which declares the middle column as a possible win line. */
    public static final int COLUMN_MIDDLE = 4;
    /** Index constant which declares the right column as a possible win line. */
    public static final int COLUMN_RIGHT = 5;
    /** Index constant which declares the increasing diagonal as a possible win line. */
    public static final int DIAGONAL_INCREASING = 6;
    /** Index constant which declares the decreasing diagonal as a possible win line. */
    public static final int DIAGONAL_DECREASING = 7;

    /** Return value describing that the game is over because there are no more winable lines. */
    public static final int GAME_OVER = -10;

    /** This method clears all fields, initialize the game state and so starts a new game. */
    public void startNewGame();

    /**
     * This method loads the already flagged fields as specified in the given game state into the
     * game logic. The state must be valid - means that the first gamer has the same number of
     * selected fields as the second gamer or one more and there must be open lines in which one or
     * both of the gamers can win the still open game.
     *
     * @param gameState an array of nine integer values representing a valid and open game state.
     * @return  the constant of the gamer having the next field selection.
     * @throws IllegalGameStateException if the game state is not valid or the game is not open.
     */
    public int startFromGameState(int[] gameState) throws IllegalGameStateException;

    /**
     * This method is the central way for the two gamers to select empty fields in an alternating
     * sequence of selections until the returned value notifies that the game is over because the
     * last gamer win the game or there are no more open lines to win the game.
     *
     * @param field selected field index from [0-8], line by line from left to right and up to down.
     * @return returns '0' for an open game, '-1' if the first gamer wins the game,
     * '+1' if the second gamer wins the game, '-2' for a double win of the first gamer,
     * '+2' for a double win of the second gamer or the GAME_OVER-constant if no one can win anymore
     * @throws IllegalFieldException if the selected field index does't point to an empty field.
     * @throws GameOverException if no more selections are possible because the game is still over.
     */
    public int setFlagToField(int field) throws IllegalFieldException, GameOverException;

    /**
     * Getter for the win line indices (length: 1 or 2) which represent some of the possible win
     * lines declared in the constants of this interface. A call of this method is only wise, if
     * there is already a winner who has reached the goal of collect win lines.
     *
     * @return 1 or 2 indices of the win lines or null if there is no winner until now.
     */
    public int[] getWinLines();

    /**
     * Getter-method to extract the current state out of this game - represented by an integer array
     * containing nine values (each one from the range [-1, 0, +1]). The inner order begins in the
     * upper left corner of the underlying 3x3-matrix and continues line by line from left to right
     * and up to down.
     *
     * @return array of nine field flags [-1, 0, +1] ordered line by line from the left upper corner
     * to the right bottom corner of the underlying 3x3-matrix representing the whole playing field.
     */
    public int[] getGameState();

    /**
     * This getter-method constructs a printable representation of the current game state which
     * contains the given characters to flag the fields owned by the first and second gamer.
     *
     * @param firstGamer the single character representing the first gamer in the returned string.
     * @param secondGamer the single character representing the second gamer in the returned string.
     * @return the customized printable string representation of the current game state.
     */
    public String getPrintableGameState(Character firstGamer, Character secondGamer);

    /**
     * This exception should be thrown if a client class try to start a game from an invalid state
     * which does't describe a possible and still open - and thereby not continuable - game state.
     */
    class IllegalGameStateException extends Exception {
    }

    /**
     * This exception should be thrown if a gamer wants to select a field which is still flagged or
     * does't exists at all (the possible field value range is [0-8]).
     */
    class IllegalFieldException extends Exception {
    }

    /**
     * This exception should be thrown if a gamer wants to select a field, while the current game is
     * already over - means there is a winner or it is no more possible that anyone win this game.
     */
    class GameOverException extends Exception {
    }
}
