package lyingAgents.utilities.exceptions;

/**
 * NameTooShortException class: Exception for when a filename is too short
 */
public class NameTooShortException extends Exception {
    /**
     * Method for the exception when a filename is too short.
     * (Settings given in SaveLoadSettings.)
     *
     * @param filename filename that will be checked
     */
    public NameTooShortException(String filename) {
        super(filename);
    }
}
