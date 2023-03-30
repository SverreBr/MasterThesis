package lyingAgents.controller.saveload;

import lyingAgents.model.Game;
import lyingAgents.utilities.Settings;
import lyingAgents.view.Popups;
import lyingAgents.utilities.GameSetting;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * LoadAction class: Contains the action that happens when the user wants to load a GameSetting.
 */
public class LoadAction extends AbstractAction {

    /**
     * Mainframe of the simulation.
     */
    private final JFrame mainFrame;

    /**
     * The game of the simulation.
     */
    private final Game game;

    /**
     * A file chooser so that the user can choose a file
     */
    private final JFileChooser fc = new JFileChooser();

    /**
     * Constructor
     *
     * @param name  Name of the button
     * @param frame Mainframe of the simulation
     * @param game  Game model
     */
    public LoadAction(String name, JFrame frame, Game game) {
        super(name);
        this.mainFrame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int fcReturnVal;
        File saveDirectory = new File(SaveLoadSettings.SAVE_DIRECTORY_NAME);
        if (!saveDirectory.exists()) {
            boolean wasSuccessful = saveDirectory.mkdir();
            if (!wasSuccessful) Popups.showGeneralException(Settings.GENERAL_EXCEPTION);
        }

        this.fc.setCurrentDirectory(saveDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Game setting files", "ser");
        fc.setFileFilter(filter);
        while (true) {
            fcReturnVal = this.fc.showOpenDialog(this.mainFrame);
            if (fcReturnVal == JFileChooser.APPROVE_OPTION) {
                File file = this.fc.getSelectedFile();
                if (loadFile(file)) {
                    Popups.showSuccessfulLoading();
                    break;
                }
            } else {
                Popups.showCancelledLoading();
                break;
            }
        }
    }

    /**
     * Method to load a file to change the game setting
     *
     * @param file The file to be loaded
     * @return True when the game setting has been successfully changed, false otherwise.
     */
    private boolean loadFile(File file) {
        try (
                FileInputStream fileInputStream = new FileInputStream(
                        file
                );
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            GameSetting gameSetting = (GameSetting) objectInputStream.readObject();
            game.newGameSettings(gameSetting);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            Popups.showIOExceptionLoading();
        } catch (Exception e) {
            Popups.showGeneralException(Settings.GENERAL_EXCEPTION);
        }
        return false;
    }
}
