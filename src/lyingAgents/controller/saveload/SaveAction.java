package lyingAgents.controller.saveload;

import lyingAgents.controller.saveload.exceptions.FileAlreadyExistsException;
import lyingAgents.controller.saveload.exceptions.ForbiddenCharacterException;
import lyingAgents.controller.saveload.exceptions.NameTooLongException;
import lyingAgents.controller.saveload.exceptions.NameTooShortException;
import lyingAgents.model.Game;
import lyingAgents.view.Popups;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SaveAction class: Saves a GameSetting to a file.
 */
public class SaveAction extends AbstractAction {

    /**
     * Mainframe of the simulation
     */
    private final JFrame frame;

    /**
     * The game model of the simulation
     */
    private final Game game;

    /**
     * Constructor
     *
     * @param name  Name of the button
     * @param frame Mainframe of the simulation
     * @param game  Game model of the simulation
     */
    public SaveAction(String name, JFrame frame, Game game) {
        super(name);
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        while (true) {
            String filename = (String) JOptionPane.showInputDialog(
                    frame, "Please insert name of the file: ",
                    "Save Game Settings", JOptionPane.PLAIN_MESSAGE,
                    null, null, "savedGame");
            if (filename != null) {
                if (this.trySaveGameSettings(filename)) {
                    Popups.showSuccessfulSaving();
                    break;
                }
            } else {
                Popups.showCancelledSaving();
                break;
            }
        }
    }

    /**
     * Method to save a game setting.
     *
     * @param filename The file name to save the game setting to.
     * @return true if saving the game setting was successful, false otherwise
     */
    private boolean trySaveGameSettings(String filename) {
        try {
            validate(filename);
            saveGameSettings(filename);
            return true;
        } catch (NameTooLongException e) {
            Popups.showErrorSaving("Filename is too long.\nPlease enter a shorter description (<" + (SaveLoadSettings.MAX_FILE_NAME + 1) + ").");
        } catch (NameTooShortException e) {
            Popups.showErrorSaving("Filename is too short.\nPlease use at least " + (SaveLoadSettings.MIN_FILE_NAME + 1) + " character(s).");
        } catch (ForbiddenCharacterException e) {
            Popups.showErrorSaving("There is a forbidden character in the filename you entered.\nPlease use only letters and numbers for the filename.");
        } catch (FileAlreadyExistsException e) {
            if (Popups.showFileNameAlreadyExistsError()) {
                saveGameSettings(filename);
                return true;
            }
        } catch (Exception e) {
            Popups.showGeneralException();
        }
        return false;
    }

    /**
     * Method to save the current game setting.
     *
     * @param fileName Name of the file where to save it to.
     */
    private void saveGameSettings(String fileName) {
        File saveDirectory = new File(SaveLoadSettings.SAVE_DIRECTORY_NAME);
        if (!saveDirectory.exists()) {
            boolean wasSuccessful = saveDirectory.mkdir();
            if (!wasSuccessful) Popups.showGeneralException();
        }

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(
                        saveDirectory + File.separator + fileName + ".ser"
                );
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(game.getGameSetting());
        } catch (IOException e) {
            Popups.showErrorSaving("Saving was unsuccessful. Did not succeed in writing the object to the file.");
        }
    }

    /**
     * Validates the name of the file the user entered.
     *
     * @param filename Name of the file
     * @throws NameTooLongException        Exception thrown when the filename is too long
     * @throws ForbiddenCharacterException Exception thrown when there is a forbidden character in the filename
     * @throws FileAlreadyExistsException  Exception thrown when the filename already exists
     * @throws NameTooShortException       Exception thrown when the name is too short
     */
    private void validate(String filename) throws NameTooLongException, ForbiddenCharacterException, FileAlreadyExistsException, NameTooShortException {

        if (filename.length() > SaveLoadSettings.MAX_FILE_NAME) throw new NameTooLongException(filename);
        if (filename.length() < SaveLoadSettings.MIN_FILE_NAME) throw new NameTooShortException(filename);

        Pattern allowedCharacters = Pattern.compile("[^A-Za-z0-9-_]");
        Matcher match = allowedCharacters.matcher(filename);
        boolean matchBoolean = match.find();
        if (matchBoolean) throw new ForbiddenCharacterException(filename);

        File tempFile = new File(SaveLoadSettings.SAVE_DIRECTORY_NAME + File.separator + filename + ".ser");
        boolean existsBoolean = tempFile.exists();
        if (existsBoolean) throw new FileAlreadyExistsException(filename);
    }
}
