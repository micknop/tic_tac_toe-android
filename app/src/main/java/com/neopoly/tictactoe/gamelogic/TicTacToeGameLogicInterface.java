package com.neopoly.tictactoe.gamelogic;

/**
 * This interface specifies all methods which each game logic class should provide to manage a
 * TicTacToe-game and support communication with client classes. In a typical TicTacToe-game two
 * gamers select in alternating sequence one empty field in a 3x3-matrix and flag it as their own.
 * The goal is to fill a whole line - means all three fields of a row, column or diagonal - with
 * the own flags before the opponent reaches this goal. In some rare cases the first gamer can
 * reach this goal for two different lines with his last selection - a 'double win'. As soon as
 * no one of the two gamers can win the current game anymore, the 'GameState.GAME_OVER' return
 * value should report this dead-end state to the client classes, so they can start a new game.
 */
interface TicTacToeGameLogicInterface {

    /** The count of all fields forming the 3x3-matrix of a TicTacToe playing field is '9'. */
    int TOTAL_FIELD_COUNT = 9;

    /** Constant for the character 'X' to customize the printable state of a game. */
    char CHAR_GAMER_X = 'X';
    /** Constant for the character 'O' to customize the printable state of a game. */
    char CHAR_GAMER_O = 'O';
    /** Constant describing the used character for empty fields in printable game states. */
    char CHAR_EMPTY_FIELD = ' ';

    /** This method clears all fields, initialize the game state and so starts a new game. */
    void startNewGame();

    /**
     * This method loads the already flagged fields as specified in the given game state into the
     * game logic. The state must be valid - means that the first gamer has the same number of
     * selected fields as the second gamer or one more and there must be open lines in which one or
     * both of the gamers can win the still open game.
     *
     * @param playingFieldState an array of nine 'FieldFlags' representing a valid and open game.
     * @return  the 'FieldFlag' of the gamer having the next field selection.
     * @throws NoInitialStateException if the playing field state is not valid or game is not open.
     */
    FieldFlag initPlayingFieldState(FieldFlag[] playingFieldState) throws NoInitialStateException;

    /**
     * This method is the central way for the two gamers to select empty fields in an alternating
     * sequence of selections until the returned value notifies that the game is over because the
     * last gamer win the game or there are no more open lines to win the game.
     *
     * @param field selected field index from [0-8], line by line from left to right and up to down.
     * @return returns a 'GameState' declaring the current state of this game
     */
    GameState setFlagToField(int field);

    /**
     * Getter for the 'win lines' (null or length: 1 or 2) which point to 'GameLine'(s). A call of
     * this method is only wise, if there is already a winner reached the goal of collect win lines.
     *
     * @return 1 or 2 'GameLine's representing the 'win lines' or null if there is no winner yet.
     */
    GameLine[] getWinLines();

    /**
     * Getter-method to extract the current state out of this game - represented by an array of
     * 'FieldFlag's containing nine values. The inner order begins in the upper left corner of
     * the underlying 3x3-matrix and continues line by line from left to right and up to down.
     *
     * @return array of nine 'FieldFlag's ordered line by line from the left upper corner to the
     * right bottom corner of the underlying 3x3-matrix representing the whole playing field.
     */
    FieldFlag[] getPlayingFieldState();

    /**
     * This getter-method constructs a printable representation of the current playing field state
     * which contains 'X's to flag the fields owned by the first gamer and 'O's to flag the fields
     * owned by the second gamer.
     *
     * @return the printable string representation of the current playing field state.
     */
    String getPrintablePlayingFieldState();

    /**
     * This getter-method constructs a printable representation of the current playing field state
     * which contains the given characters to flag the fields owned by the first and second gamer.
     *
     * @param firstGamer the single character representing the first gamer in the returned string.
     * @param secondGamer the single character representing the second gamer in the returned string.
     * @return the customized printable string representation of the current playing field state.
     */
    String getPrintablePlayingFieldState(char firstGamer, char secondGamer);

    /**
     * This exception should be thrown if a client class try to init a game from an invalid playing
     * field state which does't describes a possible, still open and thereby continuable state.
     */
    class NoInitialStateException extends Exception {}
}
