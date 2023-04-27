package lyingAgents.utilities;

/**
 * Messages class: Deals with creating messages and extracting information from a message
 */
public class Messages {

    /**
     * Prefix of a location message
     */
    private static final String LOCATION_MESSAGE_PREFIX = "My location is";

    /**
     * Splitter within a message between prefix and information
     */
    private static final String splitter = ": ";

    /**
     * Returns this when message type is not recognized
     */
    private static final String UNRECOGNIZED_MESSAGE_TYPE = "";

    /**
     * Returns this when message is of location message type
     */
    public static final String LOCATION_MESSAGE = "loc";

    /**
     * Gets the type of message
     *
     * @param message The message to get the type from
     * @return The type of the message
     */
    public static String getMessageType(String message) {
        String messageType;
        if (message.startsWith(LOCATION_MESSAGE_PREFIX)) {
            messageType = LOCATION_MESSAGE;
        } else {
            System.out.println("Message not recognized.");
            messageType = UNRECOGNIZED_MESSAGE_TYPE;
        }
        return messageType;
    }

    /**
     * Gets the location from a location message
     *
     * @param message The message received
     * @return The location that is mentioned in the message
     */
    public static int getLocationFromMessage(String message) {
        String locString = message.split(splitter, 2)[1];
        int loc;
        try {
            loc = Integer.parseInt(locString);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return loc;
    }

    /**
     * Creates a location message
     *
     * @param loc The location to be included in the message
     * @return The message including the location
     */
    public static String createLocationMessage(int loc) {
        return LOCATION_MESSAGE_PREFIX + splitter + loc;
    }
}
