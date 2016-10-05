package com.neopoly.tictactoe.gamelogic;

/**
 * This enum type bundles all possible game states a TicTacToe-game can reach
 * used to commit the current game state to any client classes.
 */
public enum GameState {
    OPEN,
    GAME_OVER,
    WINNER_FIRST,
    WINNER_SECOND,
    DOUBLE_WIN_FIRST;
}
