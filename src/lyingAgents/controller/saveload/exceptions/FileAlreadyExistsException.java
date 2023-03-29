package lyingAgents.controller.saveload.exceptions;

/**
 * FileAlreadyExistsException class: Exception for when a filename already exists
 */
public class FileAlreadyExistsException extends Exception {
    /**
     * Method for the exception when a filename already exists
     *
     * @param filename filename that will be checked
     */
    public FileAlreadyExistsException(String filename) {
        super(filename);
    }
}
