package lyingAgents.utilities;

import lyingAgents.model.Game;
import lyingAgents.view.settings.GameSettingsDialog;

import java.io.Serializable;


/**
 * GameSetting class: Creates a game setting for a game model
 */
public class GameSetting implements Serializable {

    /**
     * The board as a matrix of colored tiles
     */
    private int[][] board;

    /**
     * A matrix of chip sets for the initiator and the responder
     */
    private int[][] chipSets;

    /**
     * An array of goal positions
     */
    private int[] goalPositions;

    /**
     * Extract a game setting from information from a dialog as provided by the user
     *
     * @param bsd The dialog where the user provided information
     */
    public void getSettingsFromDialog(GameSettingsDialog bsd) {
        board = new int[Settings.BOARD_HEIGHT][Settings.BOARD_WIDTH];
        for (int row = 0; row < Settings.BOARD_WIDTH; row++) {
            for (int col = 0; col < Settings.BOARD_HEIGHT; col++) {
                board[col][row] = bsd.getIntColorTile(row, col);
            }
        }

        chipSets = new int[2][Settings.CHIP_DIVERSITY];
        for (int i = 0; i < Settings.CHIPS_PER_PLAYER; i++) {
            chipSets[0][bsd.getChipInitiator(i)] += 1;
            chipSets[1][bsd.getChipResponder(i)] += 1;
        }

        goalPositions = bsd.getGoalPositions();
    }

    /**
     * Gets game settings from a game
     *
     * @param game The game model where to take the game settings from
     */
    public void getSettingsFromGame(Game game) {
        board = game.getBoard().getBoard();
        chipSets = game.getInitialChipSets();
        goalPositions = game.getGoalPositions().clone();
    }

    /**
     * Gets the board with colored tiles
     *
     * @return A matrix representing the board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Gets the chip sets of the initiator and the responder in an array
     *
     * @return A two-dimensional array with chips from the initiator and the responder
     */
    public int[][] getChipSets() {
        return chipSets;
    }

    /**
     * Get the goal positions of the initiator and the responder
     *
     * @return A two-dimensional array with the goal positions of the initiator and the responder
     */
    public int[] getGoalPositions() {
        return goalPositions;
    }
}
