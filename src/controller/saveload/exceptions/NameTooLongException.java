package controller.saveload.exceptions;

/**
 * NameTooLongException class: Exception for if a filename is too long
 */
public class NameTooLongException extends Exception {
    /**
     * Method for the exception for if a filename is too long
     *
     * @param filename filename that will be checked
     */
    public NameTooLongException(String filename) {
        super(filename);
    }
}