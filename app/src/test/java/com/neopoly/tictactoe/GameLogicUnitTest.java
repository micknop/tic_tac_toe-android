package com.neopoly.tictactoe;

import com.neopoly.tictactoe.gamelogic.TicTacToeGameLogic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the TicTacToeGameLogic with some possible game sequences covering the important cases.
 */
public class GameLogicUnitTest {

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
        int[] expectedGameState = new int[] {0, 0, 0,                               // □|□|□
                                             0, 0, 0,                               // □|□|□
                                             0, 0, 0};                              // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());

        assertEquals(0, gameLogic.setFlagToField(0));                               // X|□|□
        expectedGameState[0] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|□

        assertEquals(0, gameLogic.setFlagToField(1));                               // X|O|□
        expectedGameState[1] = TicTacToeGameLogic.SECOND_GAMER;                     // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|□

        assertEquals(0, gameLogic.setFlagToField(2));                               // X|O|X
        expectedGameState[2] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|□

        assertEquals(0, gameLogic.setFlagToField(5));                               // X|O|X
        expectedGameState[5] = TicTacToeGameLogic.SECOND_GAMER;                     // □|□|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|□

        assertEquals(0, gameLogic.setFlagToField(8));                               // X|O|X
        expectedGameState[8] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|X

        assertEquals(0, gameLogic.setFlagToField(7));                               // X|O|X
        expectedGameState[7] = TicTacToeGameLogic.SECOND_GAMER;                     // □|□|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|O|X

        assertEquals(0, gameLogic.setFlagToField(6));                               // X|O|X
        expectedGameState[6] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|O|X

        assertEquals(0, gameLogic.setFlagToField(3));                               // X|O|X
        expectedGameState[3] = TicTacToeGameLogic.SECOND_GAMER;                     // O|□|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|O|X

        assertEquals(-2, gameLogic.setFlagToField(4));                              // X|O|X
        expectedGameState[4] = TicTacToeGameLogic.FIRST_GAMER;                      // O|X|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|O|X

        int[] winLines = gameLogic.getWinLines();
        assertEquals(2, winLines.length);                                           // X| |X
        assertEquals(TicTacToeGameLogic.DIAGONAL_INCREASING, winLines[0]);          //  |X|
        assertEquals(TicTacToeGameLogic.DIAGONAL_DECREASING, winLines[1]);          // X| |X
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
    public void setFlagToField_winFirstGamer_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        int[] expectedGameState = new int[] {0, 0, 0,                               // □|□|□
                                             0, 0, 0,                               // □|□|□
                                             0, 0, 0};                              // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());

        assertEquals(0, gameLogic.setFlagToField(8));                               // □|□|□
        expectedGameState[8] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|X

        assertEquals(0, gameLogic.setFlagToField(0));                               // O|□|□
        expectedGameState[0] = TicTacToeGameLogic.SECOND_GAMER;                     // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|X

        assertEquals(0, gameLogic.setFlagToField(6));                               // O|□|□
        expectedGameState[6] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|X

        assertEquals(0, gameLogic.setFlagToField(2));                               // O|□|O
        expectedGameState[2] = TicTacToeGameLogic.SECOND_GAMER;                     // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|X

        assertEquals(-1, gameLogic.setFlagToField(7));                              // O|□|O
        expectedGameState[7] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|X|X

        int[] winLines = gameLogic.getWinLines();                                   //  | |
        assertEquals(1, winLines.length);                                           //  | |
        assertEquals(TicTacToeGameLogic.ROW_BOTTOM, winLines[0]);                   // X|X|X
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
    public void setFlagToField_winSecondGamer_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        int[] expectedGameState = new int[] {0, 0, 0,                               // □|□|□
                                             0, 0, 0,                               // □|□|□
                                             0, 0, 0};                              // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());

        assertEquals(0, gameLogic.setFlagToField(6));                               // □|□|□
        expectedGameState[6] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(2));                               // □|□|O
        expectedGameState[2] = TicTacToeGameLogic.SECOND_GAMER;                     // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(0));                               // X|□|O
        expectedGameState[0] = TicTacToeGameLogic.FIRST_GAMER;                      // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(3));                               // X|□|O
        expectedGameState[3] = TicTacToeGameLogic.SECOND_GAMER;                     // O|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(4));                               // X|□|O
        expectedGameState[4] = TicTacToeGameLogic.FIRST_GAMER;                      // O|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(8));                               // X|□|O
        expectedGameState[8] = TicTacToeGameLogic.SECOND_GAMER;                     // O|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|O

        assertEquals(0, gameLogic.setFlagToField(7));                               // X|□|O
        expectedGameState[7] = TicTacToeGameLogic.FIRST_GAMER;                      // O|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|X|O

        assertEquals(1, gameLogic.setFlagToField(5));                               // X|□|O
        expectedGameState[5] = TicTacToeGameLogic.SECOND_GAMER;                     // O|X|O
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|X|O

        int[] winLines = gameLogic.getWinLines();                                   //  | |O
        assertEquals(1, winLines.length);                                           //  | |O
        assertEquals(TicTacToeGameLogic.COLUMN_RIGHT, winLines[0]);                 //  | |O
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
        int[] expectedGameState = new int[] {0, 0, 0,                               // □|□|□
                                             0, 0, 0,                               // □|□|□
                                             0, 0, 0};                              // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());

        assertEquals(0, gameLogic.setFlagToField(4));                               // □|□|□
        expectedGameState[4] = TicTacToeGameLogic.FIRST_GAMER;                      // □|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|□

        assertEquals(0, gameLogic.setFlagToField(0));                               // O|□|□
        expectedGameState[0] = TicTacToeGameLogic.SECOND_GAMER;                     // □|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // □|□|□

        assertEquals(0, gameLogic.setFlagToField(6));                               // O|□|□
        expectedGameState[6] = TicTacToeGameLogic.FIRST_GAMER;                      // □|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(2));                               // O|□|O
        expectedGameState[2] = TicTacToeGameLogic.SECOND_GAMER;                     // □|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(1));                               // O|X|O
        expectedGameState[1] = TicTacToeGameLogic.FIRST_GAMER;                      // □|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|□|□

        assertEquals(0, gameLogic.setFlagToField(7));                               // O|X|O
        expectedGameState[7] = TicTacToeGameLogic.SECOND_GAMER;                     // □|X|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|O|□

        assertEquals(0, gameLogic.setFlagToField(5));                               // O|X|O
        expectedGameState[5] = TicTacToeGameLogic.FIRST_GAMER;                      // □|X|X
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|O|□

        assertEquals(TicTacToeGameLogic.GAME_OVER, gameLogic.setFlagToField(3));    // O|X|O
        expectedGameState[3] = TicTacToeGameLogic.SECOND_GAMER;                     // O|X|X
        assertArrayEquals(expectedGameState, gameLogic.getGameState());             // X|O|□

        assertNull(gameLogic.getWinLines());                                // No 'win lines'
    }

    @Test
    /**
     * Test to load a game state and continue by setting the double win field for first gamer:
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
    public void startFromGameState_and_startNewGame_isCorrect() throws Exception {

        TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
        int[] initGameState = new int[] { 0, -1, -1,                                // □|X|X
                                         -1,  1,  1,                                // X|O|O
                                         -1,  1,  1};                               // X|O|O

        assertEquals(TicTacToeGameLogic.FIRST_GAMER, gameLogic.startFromGameState(initGameState));

        int[] expectedGameState = new int[] {-1, -1, -1,                            // X|X|X
                                             -1,  1,  1,                            // X|O|O
                                             -1,  1,  1};                           // X|O|O

        assertEquals(-2, gameLogic.setFlagToField(0));
        assertArrayEquals(expectedGameState, gameLogic.getGameState());

        int[] winLines = gameLogic.getWinLines();
        assertEquals(2, winLines.length);                                           // X|X|X
        assertEquals(TicTacToeGameLogic.ROW_HEAD, winLines[0]);                     // X| |
        assertEquals(TicTacToeGameLogic.COLUMN_LEFT, winLines[1]);                  // X| |

        // Test if the restart of a game works correct
        gameLogic.startNewGame();
        expectedGameState = new int[] {0, 0, 0,                                     // □|□|□
                                       0, 0, 0,                                     // □|□|□
                                       0, 0, 0};                                    // □|□|□
        assertArrayEquals(expectedGameState, gameLogic.getGameState());
    }
}