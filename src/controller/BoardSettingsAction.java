package controller;

import view.settings.BoardSettingsDialog;
import view.settings.GameSetting;
import model.Game;
import view.Popups;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * BoardSettingsAction class: Action when button is clicked to change the board of the game.
 */
public class BoardSettingsAction extends AbstractAction {

    /**
     * Game model of the simulation
     */
    private final Game game;

    /**
     * Mainframe of the simulation
     */
    private final JFrame mainFrame;

    /**
     * Constructor
     *
     * @param description Name of the action
     * @param game        Game model of the simulation
     * @param mainFrame   Mainframe of the simulation
     */
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
            GameSetting gameSetting = new GameSetting();
            gameSetting.getSettingsFromDialog(bsd);
            game.newGameSettings(gameSetting);
        }
        bsd.dispose();
    }
}
