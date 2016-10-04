package com.neopoly.tictactoe.gamelogic;

/**
 * This abstract game logic class for TicTacToe-games implements some comfort methods which can be
 * performed independent from any inner representations and computation steps of explicit game logic
 * classes. In detail this is validation and following loading of game states, as well as the
 * construction of a printable game state representation.
 */
public abstract class AbstractTicTacToeGameLogic implements TicTacToeGameLogicInterface {

    @Override
    public String getPrintableGameState(Character firstGamer, Character secondGamer) {

        Character[] usableChars = new Character[] {firstGamer, CHAR_EMPTY_FIELD, secondGamer};
        Character[] fieldChars = new Character[TOTAL_FIELD_COUNT];
        int[] gameState = getGameState();

        // Translate the nine integer values of the game state array into a array of nine characters
        for (int field = 0; field < TOTAL_FIELD_COUNT; field++) {
            // ...by replacing:
            // all '-1' with the delivered 'firstGamer'-Character,    // index '0' in 'usableChars'
            // all '0' with the constant 'CHAR_EMPTY_FIELD' and       // index '1' in 'usableChars'
            // all '+1' with the delivered 'secondGamer'-Character.   // index '2' in 'usableChars'
            // ...meaning: 'field-value + 1' is index of each Character in the 'usableChars'-array
            fieldChars[field] = usableChars[gameState[field] + 1];
        }

        // The constructed printable game state looks like this:
        //
        //  O | X | O
        // ---|---|---
        //    | X |
        // ---|---|---
        //  X | O | X
        //
        return " \n " + fieldChars[0] + " | " + fieldChars[1] + " | " + fieldChars[2] + " \n"
                + "---|---|---\n"
                + " " + fieldChars[3] + " | " + fieldChars[4] + " | " + fieldChars[5] + " \n"
                + "---|---|---\n"
                + " " + fieldChars[6] + " | " + fieldChars[7] + " | " + fieldChars[8] + " \n";
    }

    @Override
    public int startFromGameState(int[] gameState) throws IllegalGameStateException{

        int nextGamer = FIRST_GAMER;
        startNewGame();

        if (gameState != null && gameState.length == TOTAL_FIELD_COUNT  // Check number of fields
                && gameState[0] >= -1 && gameState[0] <= 1
                && gameState[1] >= -1 && gameState[1] <= 1
                && gameState[2] >= -1 && gameState[2] <= 1
                && gameState[3] >= -1 && gameState[3] <= 1      // Check that each field's value
                && gameState[4] >= -1 && gameState[4] <= 1      // equals one of the three values:
                && gameState[5] >= -1 && gameState[5] <= 1      // '-1', '0' or '+1'
                && gameState[6] >= -1 && gameState[6] <= 1
                && gameState[7] >= -1 && gameState[7] <= 1
                && gameState[8] >= -1 && gameState[8] <= 1) {

            // Collect the indices of all fields flagged by the first an the second gamer
            int[] fieldIndicesOfFirstGamer = new int[] {4};
            int[] fieldIndicesOfSecondGamer = new int[] {4};
            int fieldCountOfFirstGamer = 0;
            int fieldCountOfSecondGamer = 0;
            int currentFieldValue;
            for (int fieldIndex = 0; fieldIndex < TOTAL_FIELD_COUNT; fieldIndex++) {
                currentFieldValue = gameState[fieldIndex];
                if (currentFieldValue == FIRST_GAMER) {
                    fieldIndicesOfFirstGamer[fieldCountOfFirstGamer] = fieldIndex;
                    fieldCountOfFirstGamer++;
                } else if (currentFieldValue == SECOND_GAMER) {
                    fieldIndicesOfSecondGamer[fieldCountOfSecondGamer] = fieldIndex;
                    fieldCountOfSecondGamer++;
                }
            }

            // Check if the number of flagged fields is either equal or the first gamer has one more
            if (fieldCountOfFirstGamer == fieldCountOfSecondGamer
                    || fieldCountOfFirstGamer == (fieldCountOfSecondGamer + 1)) {
                // Try to insert all flags into the new game - order is irrelevant for finally state
                try {
                    int returnValue = 0;
                    for (int round = 0; round < fieldCountOfSecondGamer; round++) {
                        setFlagToField(fieldIndicesOfFirstGamer[round]);
                        returnValue = setFlagToField(fieldIndicesOfSecondGamer[round]);
                    }
                    if (fieldCountOfFirstGamer > fieldCountOfSecondGamer) {
                        returnValue = setFlagToField(
                                fieldIndicesOfFirstGamer[fieldCountOfFirstGamer - 1]);
                        nextGamer = SECOND_GAMER;
                    }
                    if (returnValue != 0) {
                        throw new IllegalGameStateException();
                    }
                } catch (Exception e) {
                    throw new IllegalGameStateException();
                }
            } else throw new IllegalGameStateException();

        } else throw new IllegalGameStateException();

        return nextGamer;
    }
}
