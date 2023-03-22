package controller.saveload;

import controller.saveload.exceptions.FileAlreadyExistsException;
import controller.saveload.exceptions.ForbiddenCharacterException;
import controller.saveload.exceptions.NameTooLongException;
import controller.saveload.exceptions.NameTooShortException;
import model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveAction extends AbstractAction {

    private final JFrame frame;
    private final Game game;

    private static final String SAVE_DIRECTORY_NAME = "saved_game_settings";

    public SaveAction(String name, JFrame frame, Game game) {
        super(name);
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        File wk = new File(System.getProperty("user.dir"));
//        this.fc.setCurrentDirectory(wk);
//        int returnVal = this.fc.showOpenDialog(this.frame);
        String filename = (String)JOptionPane.showInputDialog(
                frame,
                "Please insert name of the file: ",
                "Save Game Settings",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "savedGame");
        System.out.println(filename);
        if (filename != null) {
            if (this.trySaveGameSettings(filename)) {
                System.out.println("Export successful");
            } else {
                System.out.println("Export not successful");
            }
        } else {
            System.out.println("cancelled");
        }
    }

    private boolean trySaveGameSettings(String filename) {
        try {
            validate(filename);
            saveGameSettings(filename);
            return true;
        } catch (NameTooLongException e) {
            System.out.println("Filename is too long. Game did not save.");
        } catch (NameTooShortException e) {
            System.out.println("Filename is too short. Game did not save.");
        } catch (ForbiddenCharacterException e) {
            System.out.println("There is a forbidden character in this filename.\nGame did not save.");
        } catch (FileAlreadyExistsException e) {
            System.out.println("This filename already exists.");
        } catch (Exception e) {
            System.out.println("Something went wrong with saving the game!");
        }
        return false;
    }

    private void saveGameSettings(String fileName) throws SecurityException {
        File saveDirectory = new File(SAVE_DIRECTORY_NAME);
        if (!saveDirectory.mkdir()) {
            System.out.println("Directory was not correctly made.");
        }

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(
                        saveDirectory + File.separator + fileName + ".ser"
                );
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(game.getGameSetting());
            System.out.println("Save successful");
        } catch (IOException e) {
            System.out.println("Failed or interrupted I/O operations");
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
