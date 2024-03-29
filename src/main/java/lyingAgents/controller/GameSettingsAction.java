package lyingAgents.controller;

import lyingAgents.view.changeSettings.GameSettingsDialog;
import lyingAgents.utilities.GameSetting;
import lyingAgents.model.Game;
import lyingAgents.view.Popups;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * BoardSettingsAction class: Action when button is clicked to change the board of the game.
 */
public class GameSettingsAction extends AbstractAction {

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
    public GameSettingsAction(String description, Game game, JFrame mainFrame) {
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

        GameSettingsDialog bsd = new GameSettingsDialog(mainFrame, "Game settings", game);
        bsd.setVisible(true);

        if (bsd.isGameHasChanged()) {
            GameSetting gameSetting = new GameSetting(bsd);
            game.newGameSettings(gameSetting);
        }
        bsd.dispose();
    }
}
