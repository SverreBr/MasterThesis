package controller;

import gui.SettingsDialog;
import utilities.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettingsAction extends AbstractAction {

    private final Game game;

    private final JFrame mainFrame;

    public SettingsAction(String description, Game game, JFrame mainFrame) {
        super(description);
        this.game = game;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int initToM, respToM;
        double initLR, respLR;

        SettingsDialog sd = new SettingsDialog(mainFrame,"Game settings");
        sd.setVisible(true);

        if (sd.gameHasChanged) {
            initToM = sd.getInitiatorToMFieldValue();
            initLR = sd.getInitiatorLRFieldValue();
            respToM = sd.getResponderToMFieldValue();
            respLR = sd.getResponderLRFieldValue();
            game.reset(initToM, respToM, initLR, respLR);
        }
    }
}
