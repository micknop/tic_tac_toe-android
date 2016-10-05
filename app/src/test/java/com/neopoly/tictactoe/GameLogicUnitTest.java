package com.neopoly.tictactoe;

import com.neopoly.tictactoe.gamelogic.FieldFlag;
import com.neopoly.tictactoe.gamelogic.GameLine;
import com.neopoly.tictactoe.gamelogic.GameState;
import com.neopoly.tictactoe.gamelogic.TicTacToeGameLogic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the TicTacToeGameLogic with some possible game sequences covering the important cases.
 */
public class GameLogicUnitTest {

    /**An abbreviated notation for 'FieldFlag.FIRST_GAMERS_FLAG'. */
    private static final FieldFlag X = FieldFlag.FIRST_GAMERS_FLAG;
    /**An abbreviated notation for 'FieldFlag.SECOND_GAMERS_FLAG'. */
    private static final FieldFlag O = FieldFlag.SECOND_GAMERS_FLAG;
    /**An abbreviated notation for 'FieldFlag.EMPTY_FIELD'. */
    private static final FieldFlag E = FieldFlag.EMPTY_FIELD;

    @Test
    /**
     * Test a game sequence in which all fields get a flag from the upper left clockwise to the
     * centered field - leading to a 'double win' for the first gamer in both diagonals:
     *
     *  X | O | X       X | O | X
     * ---|---|---     ---|---|---
     *  O |   | O  -->  O | X | O
     * ---|---|---     ---|---|---
     *  X | O | X       X | O | X
     *
     * ...where 'X' is the first gamer and 'O' the second.
     */
    public void setFlagToField_doubleWin_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        FieldFlag[] expectedPlayingFieldState = new FieldFlag[] {E, E, E,               // □|□|□
                                                                 E, E, E,               // □|□|□
                                                                 E, E, E};              // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState());

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(0));                      // X|□|□
        expectedPlayingFieldState[0] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(1));                      // X|O|□
        expectedPlayingFieldState[1] = O;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(2));                      // X|O|X
        expectedPlayingFieldState[2] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(5));                      // X|O|X
        expectedPlayingFieldState[5] = O;                                               // □|□|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(8));                      // X|O|X
        expectedPlayingFieldState[8] = X;                                               // □|□|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|X

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(7));                      // X|O|X
        expectedPlayingFieldState[7] = O;                                               // □|□|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|O|X

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(6));                      // X|O|X
        expectedPlayingFieldState[6] = X;                                               // □|□|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|O|X

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(3));                      // X|O|X
        expectedPlayingFieldState[3] = O;                                               // O|□|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|O|X

        assertEquals(GameState.DOUBLE_WIN_FIRST, gameLogic.setFlagToField(4));          // X|O|X
        expectedPlayingFieldState[4] = X;                                               // O|X|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|O|X

        GameLine[] winLines = gameLogic.getWinLines();
        assertEquals(2, winLines.length);                                               // X| |X
        assertEquals(GameLine.DIAGONAL_INCREASING, winLines[0]);                        //  |X|
        assertEquals(GameLine.DIAGONAL_DECREASING, winLines[1]);                        // X| |X
    }

    @Test
    /**
     * Test a game sequence in which the first gamer wins the game after fife selection rounds:
     *
     *  O |   | O       O |   | O
     * ---|---|---     ---|---|---
     *    |   |    -->    |   |
     * ---|---|---     ---|---|---
     *  X |   | X       X | X | X
     *
     * ...where 'X' is the first gamer and 'O' the second.
     */
    public void setFlagToField_winnerFirst_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        FieldFlag[] expectedPlayingFieldState = new FieldFlag[] {E, E, E,               // □|□|□
                                                                 E, E, E,               // □|□|□
                                                                 E, E, E};              // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState());

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(8));                      // □|□|□
        expectedPlayingFieldState[8] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|X

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(0));                      // O|□|□
        expectedPlayingFieldState[0] = O;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|X

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(6));                      // O|□|□
        expectedPlayingFieldState[6] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|X

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(2));                      // O|□|O
        expectedPlayingFieldState[2] = O;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|X

        assertEquals(GameState.WINNER_FIRST, gameLogic.setFlagToField(7));              // O|□|O
        expectedPlayingFieldState[7] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|X|X

        GameLine[] winLines = gameLogic.getWinLines();                                  //  | |
        assertEquals(1, winLines.length);                                               //  | |
        assertEquals(GameLine.ROW_BOTTOM, winLines[0]);                                 // X|X|X
    }

    @Test
    /**
     * Test a game sequence in which the second gamer wins the game after eight selection rounds:
     *
     *  X |   | O       X |   | O
     * ---|---|---     ---|---|---
     *  O | X |    -->  O | X | O
     * ---|---|---     ---|---|---
     *  X | X | O       X | X | O
     *
     * ...where 'X' is the first gamer and 'O' the second.
     */
    public void setFlagToField_winnerSecond_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        FieldFlag[] expectedPlayingFieldState = new FieldFlag[] {E, E, E,               // □|□|□
                                                                 E, E, E,               // □|□|□
                                                                 E, E, E};              // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState());

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(6));                      // □|□|□
        expectedPlayingFieldState[6] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(2));                      // □|□|O
        expectedPlayingFieldState[2] = O;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(0));                      // X|□|O
        expectedPlayingFieldState[0] = X;                                               // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(3));                      // X|□|O
        expectedPlayingFieldState[3] = O;                                               // O|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(4));                      // X|□|O
        expectedPlayingFieldState[4] = X;                                               // O|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(8));                      // X|□|O
        expectedPlayingFieldState[8] = O;                                               // O|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|O

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(7));                      // X|□|O
        expectedPlayingFieldState[7] = X;                                               // O|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|X|O

        assertEquals(GameState.WINNER_SECOND, gameLogic.setFlagToField(5));             // X|□|O
        expectedPlayingFieldState[5] = O;                                               // O|X|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|X|O

        GameLine[] winLines = gameLogic.getWinLines();                                  //  | |O
        assertEquals(1, winLines.length);                                               //  | |O
        assertEquals(GameLine.COLUMN_RIGHT, winLines[0]);                               //  | |O
    }

    @Test
    /**
     * Test a game sequence in which the game is over after eight selected fields without a winner:
     *
     *  O | X | O       O | X | O
     * ---|---|---     ---|---|---
     *    | X | X  -->  O | X | X
     * ---|---|---     ---|---|---
     *  X | O |         X | O |
     *
     * ...where 'X' is the first gamer and 'O' the second.
     */
    public void setFlagToField_gameOver_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        FieldFlag[] expectedPlayingFieldState = new FieldFlag[] {E, E, E,               // □|□|□
                                                                 E, E, E,               // □|□|□
                                                                 E, E, E};              // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState());

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(4));                      // □|□|□
        expectedPlayingFieldState[4] = X;                                               // □|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(0));                      // O|□|□
        expectedPlayingFieldState[0] = O;                                               // □|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // □|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(6));                      // O|□|□
        expectedPlayingFieldState[6] = X;                                               // □|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(2));                      // O|□|O
        expectedPlayingFieldState[2] = O;                                               // □|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(1));                      // O|X|O
        expectedPlayingFieldState[1] = X;                                               // □|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|□|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(7));                      // O|X|O
        expectedPlayingFieldState[7] = O;                                               // □|X|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|O|□

        assertEquals(GameState.OPEN, gameLogic.setFlagToField(5));                      // O|X|O
        expectedPlayingFieldState[5] = X;                                               // □|X|X
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|O|□

        assertEquals(GameState.GAME_OVER, gameLogic.setFlagToField(3));                 // O|X|O
        expectedPlayingFieldState[3] = O;                                               // O|X|X
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState()); // X|O|□

        assertNull(gameLogic.getWinLines());                                    // No 'win lines'
    }

    @Test
    /**
     * Test to load a playing field state and continue setting the double win field for first gamer:
     *
     *    | X | X       X | X | X
     * ---|---|---     ---|---|---
     *  X | O | O  -->  X | O | O
     * ---|---|---     ---|---|---
     *  X | O | O       X | O | O
     *
     * ...where 'X' is the first gamer and 'O' the second.
     *
     * Additionally test the 'startNewGame()'-method if correct restarting the game.
     */
    public void initPlayingFieldState_and_startNewGame_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        FieldFlag[] initialPlayingFieldState = new FieldFlag[] {E, X, X,                // □|X|X
                                                                X, O, O,                // X|O|O
                                                                X, O, O};               // X|O|O
        assertEquals(FieldFlag.FIRST_GAMERS_FLAG, 
                gameLogic.initPlayingFieldState(initialPlayingFieldState));
        
        assertEquals(GameState.DOUBLE_WIN_FIRST, gameLogic.setFlagToField(0));
        FieldFlag[] expectedPlayingFieldState = new FieldFlag[] {X, X, X,               // X|X|X
                                                                 X, O, O,               // X|O|O
                                                                 X, O, O};              // X|O|O
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState());

        GameLine[] winLines = gameLogic.getWinLines();
        assertEquals(2, winLines.length);                                               // X|X|X
        assertEquals(GameLine.ROW_HEAD, winLines[0]);                                   // X| |
        assertEquals(GameLine.COLUMN_LEFT, winLines[1]);                                // X| |

        // Test if the restart of a game works correct
        gameLogic.startNewGame();
        expectedPlayingFieldState = new FieldFlag[] {E, E, E,                           // □|□|□
                                                     E, E, E,                           // □|□|□
                                                     E, E, E};                          // □|□|□
        assertArrayEquals(expectedPlayingFieldState, gameLogic.getPlayingFieldState());
    }
}