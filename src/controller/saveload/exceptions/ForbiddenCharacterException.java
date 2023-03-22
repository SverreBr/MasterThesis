package controller.saveload.exceptions;

/**
 * ForbiddenCharacterException class: Exception for if a filename contains a forbidden character
 */
public class ForbiddenCharacterException extends Exception {
    /**
     * Method for the exception for if a filename contains a
     * forbidden character
     *
     * @param filename filename that will be checked
     */
    public ForbiddenCharacterException(String filename) {
        super(filename);
    }
}