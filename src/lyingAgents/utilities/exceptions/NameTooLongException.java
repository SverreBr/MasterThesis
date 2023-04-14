package lyingAgents.utilities.exceptions;

/**
 * NameTooLongException class: Exception for when a filename is too long
 */
public class NameTooLongException extends Exception {
    /**
     * Method for the exception when a filename is too long.
     * (Setting given in SaveLoadSettings.)
     *
     * @param filename filename that will be checked
     */
    public NameTooLongException(String filename) {
        super(filename);
    }
}