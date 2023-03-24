package controller.saveload;

import controller.saveload.exceptions.FileAlreadyExistsException;
import controller.saveload.exceptions.ForbiddenCharacterException;
import controller.saveload.exceptions.NameTooLongException;
import controller.saveload.exceptions.NameTooShortException;
import model.Game;
import view.Popups;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveAction extends AbstractAction {

    private final JFrame frame;
    private final Game game;

    private static final String SAVE_DIRECTORY_NAME = "saved_gameSettings";

    public SaveAction(String name, JFrame frame, Game game) {
        super(name);
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean error = true;
        while (error) {
            String filename = (String) JOptionPane.showInputDialog(
                    frame,
                    "Please insert name of the file: ",
                    "Save Game Settings",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "savedGame");
            if (filename != null) {
                if (this.trySaveGameSettings(filename)) {
                    Popups.showSuccessfulSaving();
                    error = false;
                }
            } else {
                Popups.showCancelledSaving();
                error = false;
            }
        }
    }

    private boolean trySaveGameSettings(String filename) {
        try {
            validate(filename);
            saveGameSettings(filename);
            return true;
        } catch (NameTooLongException e) {
            Popups.showErrorSaving("Filename is too long.\nPlease enter a shorter description.");
        } catch (NameTooShortException e) {
            Popups.showErrorSaving("Filename is too short.\nPlease use at least one character.");
        } catch (ForbiddenCharacterException e) {
            Popups.showErrorSaving("There is a forbidden character in the filename you entered.\nPlease use only letters and numbers for the filename.");
        } catch (FileAlreadyExistsException e) {
            if (Popups.showFileNameAlreadyExistsError("The filename you entered already exists.\nDo you want to overwrite the existing file?")) {
                saveGameSettings(filename);
                return true;
            }
        } catch (Exception e) {
            Popups.showGeneralException();
        }
        return false;
    }

    private void saveGameSettings(String fileName) throws SecurityException {
        File saveDirectory = new File(SAVE_DIRECTORY_NAME);
        saveDirectory.mkdir();

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

    private void validate(String filename) throws NameTooLongException, ForbiddenCharacterException, FileAlreadyExistsException, NameTooShortException {
        int MAX_FILE_NAME = 10;
        int MIN_FILE_NAME = 1;
        if (filename.length() > MAX_FILE_NAME) {
            throw new NameTooLongException(filename);
        }

        if (filename.length() < MIN_FILE_NAME) {
            throw new NameTooShortException(filename);
        }

        Pattern allowedCharacters = Pattern.compile("[^A-Za-z0-9]");
        Matcher match = allowedCharacters.matcher(filename);
        boolean matchBoolean = match.find();
        if (matchBoolean) {
            throw new ForbiddenCharacterException(filename);
        }

        File tempFile = new File(SAVE_DIRECTORY_NAME + File.separator + filename + ".ser");
        boolean existsBoolean = tempFile.exists();
        if (existsBoolean) {
            throw new FileAlreadyExistsException(filename);
        }
    }
}
