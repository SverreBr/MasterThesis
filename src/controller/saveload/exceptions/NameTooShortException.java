package controller.saveload.exceptions;

/**
 * NameTooShortException class: Exception for if a filename is too short
 */
public class NameTooShortException extends Exception {
    /**
     * Method for the exception for if a filename is too short
     *
     * @param filename filename that will be checked
     */
    public NameTooShortException(String filename) {
        super(filename);
    }
}
