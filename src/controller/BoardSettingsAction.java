package controller;

import gui.settings.BoardSettingsDialog;
import model.Game;
import gui.Popups;
import utilities.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BoardSettingsAction extends AbstractAction {

    private final Game game;
    private final JFrame mainFrame;

    public BoardSettingsAction(String description, Game game, JFrame mainFrame) {
        super(description);
        this.game = game;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (game.isSimulationOff()) {
            Popups.showSettingsButtonNotAccessible();
            return;
        }

        BoardSettingsDialog bsd = new BoardSettingsDialog(mainFrame, "Game settings");
        bsd.setVisible(true);
        if (bsd.isGameHasChanged()) {
            int[][] board = new int[Settings.BOARD_HEIGHT][Settings.BOARD_WIDTH];
            System.out.println("game changed.");

            for (int row = 0; row < Settings.BOARD_WIDTH; row++) {
                for (int col = 0; col < Settings.BOARD_HEIGHT; col++) {
                    board[col][row] = bsd.getIntColorTile(row, col);
                }
            }
            game.newBoard(board, bsd.getGoalPositions());
        }
        bsd.dispose();
    }
}
