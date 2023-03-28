package controller;

import view.settings.SettingsDialog;
import view.Popups;
import model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * SettingsAction class: deals with changing settings via the settings button
 */
public class SettingsAction extends AbstractAction {

    /**
     * The game model
     */
    private final Game game;

    /**
     * The main frame of the visuals
     */
    private final JFrame mainFrame;

    /**
     * Constructor
     *
     * @param description Name of the action
     * @param game        Game model
     * @param mainFrame   Main frame of the visuals
     */
    public SettingsAction(String description, Game game, JFrame mainFrame) {
        super(description);
        this.game = game;
        this.mainFrame = mainFrame;
    }

    /**
     * Method called when the settings of the agents are changed based on the settingsDialog.
     *
     * @param sd SettingsDialog
     */
    private void changeGame(SettingsDialog sd) {
        int initToM, respToM;
        double initLR, respLR;
        boolean initCanLie, respCanLie;

        initToM = sd.getInitiatorToMFieldValue();
        initLR = sd.getInitiatorLRFieldValue();
        initCanLie = sd.isInitiatorLieFieldValue();
        respToM = sd.getResponderToMFieldValue();
        respLR = sd.getResponderLRFieldValue();
        respCanLie = sd.isResponderLieFieldValue();

        game.reset(initToM, respToM, initLR, respLR, initCanLie, respCanLie);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (game.isSimulationOff()) {
            Popups.showSettingsButtonNotAccessible();
            return;
        }

        SettingsDialog sd = new SettingsDialog(mainFrame, "Game player settings");
        sd.setVisible(true);

        if (sd.gameHasChanged) changeGame(sd);
        sd.dispose();
    }
}
