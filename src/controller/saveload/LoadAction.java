package controller.saveload;

import model.Game;
import view.Popups;
import view.settings.GameSetting;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadAction extends AbstractAction {

    private final JFrame frame;
    private final Game game;

    private GameSetting gameSetting;

    private final JFileChooser fc = new JFileChooser();

    public LoadAction(String name, JFrame frame, Game game) {
        super(name);
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File wk = new File(System.getProperty("user.dir"));
        this.fc.setCurrentDirectory(wk);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Game setting files", "ser");
        fc.setFileFilter(filter);
        boolean error = true;
        while (error) {
            int returnVal = this.fc.showOpenDialog(this.frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = this.fc.getSelectedFile();
                if (loadFile(file)) {
                    Popups.showSuccessfulLoading();
                    error = false;
                }
            } else {
                Popups.showCancelledLoading();
                error = false;
            }
        }
    }

    private boolean loadFile(File file) {
        try (
                FileInputStream fileInputStream = new FileInputStream(
                        file
                );
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            gameSetting = (GameSetting) objectInputStream.readObject();
            changeGameSetting();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            Popups.showIOExceptionLoading();
        } catch (Exception e) {
            Popups.showGeneralException();
        }
        gameSetting = null;
        return false;
    }

    private void changeGameSetting() {
        game.newGameSettings(gameSetting);
    }
}
