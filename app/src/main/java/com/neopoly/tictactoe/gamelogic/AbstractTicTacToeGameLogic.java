package com.neopoly.tictactoe.gamelogic;

/**
 * This abstract game logic class for TicTacToe-games implements some comfort methods which can be
 * performed independent from any inner representations and computation steps of explicit game logic
 * classes. In detail this is validation and following loading of playing field states, as well as
 * the construction of a printable game state representation.
 */
abstract class AbstractTicTacToeGameLogic implements TicTacToeGameLogicInterface {

    @Override
    public String getPrintablePlayingFieldState() {
        return getPrintablePlayingFieldState(CHAR_GAMER_X, CHAR_GAMER_O);
    }

    @Override
    public String getPrintablePlayingFieldState(char firstGamerChar, char secondGamerChar) {

        char[] fieldChars = new char[TOTAL_FIELD_COUNT];
        FieldFlag[] playingFieldState = getPlayingFieldState();

        // Translate the game state's 'FieldFlag'-values into an array of nine characters
        for (int field = 0; field < TOTAL_FIELD_COUNT; field++) {
            switch (playingFieldState[field]) {
                case EMPTY_FIELD:
                    fieldChars[field] = CHAR_EMPTY_FIELD;
                    break;
                case FIRST_GAMERS_FLAG:
                    fieldChars[field] = firstGamerChar;
                    break;
                case SECOND_GAMERS_FLAG:
                    fieldChars[field] = secondGamerChar;
            }
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
    public FieldFlag initPlayingFieldState(FieldFlag[] playingFieldState)
            throws NoInitialStateException{

        FieldFlag nextGamer = FieldFlag.FIRST_GAMERS_FLAG;
        startNewGame();

        // Check number of fields
        if (playingFieldState != null && playingFieldState.length == TOTAL_FIELD_COUNT) {

            int[] flaggedByFirst = new int[4];  // max. 4 fields owned by first gamer in open games
            int countFirstsFlags = 0;
            int[] flaggedBySecond = new int[4]; // max. 4 fields owned by second gamer in open game
            int countSecondsFlags = 0;

            // Collect the indices of all fields flagged by the first or the second gamer
            for (int fieldIndex = 0; fieldIndex < TOTAL_FIELD_COUNT; fieldIndex++) {
                switch (playingFieldState[fieldIndex]) {
                    case FIRST_GAMERS_FLAG:
                        flaggedByFirst[countFirstsFlags] = fieldIndex;
                        countFirstsFlags++;
                        break;
                    case SECOND_GAMERS_FLAG:
                        flaggedBySecond[countSecondsFlags] = fieldIndex;
                        countSecondsFlags++;
                }
            }

            // Check if the number of flagged fields is either equal or the first gamer has one more
            if (countFirstsFlags == countSecondsFlags
                    || countFirstsFlags == (countSecondsFlags + 1)) {

                // Insert all flags into the new game - order is irrelevant for finally state
                GameState currentGameState = GameState.OPEN;
                for (int round = 0;
                     currentGameState.equals(GameState.OPEN)
                        && round < countSecondsFlags; round++) {

                    setFlagToField(flaggedByFirst[round]);
                    currentGameState = setFlagToField(flaggedBySecond[round]);
                }
                if (countFirstsFlags > countSecondsFlags) {
                    nextGamer = FieldFlag.SECOND_GAMERS_FLAG;
                    currentGameState = setFlagToField(flaggedByFirst[countFirstsFlags - 1]);
                }

                if (!currentGameState.equals(GameState.OPEN)) {
                    throw new NoInitialStateException();
                }
            } else throw new NoInitialStateException();

        } else throw new NoInitialStateException();

        return nextGamer;
    }
}
