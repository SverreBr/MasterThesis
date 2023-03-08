package utilities;

public class Messages {

    private static final String LOCATION_MESSAGE_PREFIX = "My location is";
    private static final String splitter = ": ";
    private static final String UNRECOGNIZED_MESSAGE_TYPE = "";

    public static final String LOCATION_MESSAGE = "loc";

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

    public static String createLocationMessage(int loc) {
        return LOCATION_MESSAGE_PREFIX + splitter + loc;
    }
}
