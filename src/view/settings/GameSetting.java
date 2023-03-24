package view.settings;

import model.Game;
import utilities.Settings;

import java.io.Serializable;

public class GameSetting implements Serializable {

    private int[][] board;

    private int[][] chipSets;

    private int[] goalPositions;

    public void getSettingsFromDialog(BoardSettingsDialog bsd) {
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

    public void getSettingsFromGame(Game game) {
        board = game.getBoard().getBoard();
        chipSets = game.getInitialChipSets();
        goalPositions = game.getGoalPositions().clone();
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getChipSets() {
        return chipSets;
    }

    public int[] getGoalPositions() {
        return goalPositions;
    }
}
