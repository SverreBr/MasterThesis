package controller;

import view.settings.BoardSettingsDialog;
import view.settings.GameSetting;
import model.Game;
import view.Popups;

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


            GameSetting gameSetting = new GameSetting();
            gameSetting.getSettingsFromDialog(bsd);
            game.newGameSettings(gameSetting);

        }
        bsd.dispose();
    }
}
