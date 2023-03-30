package lyingAgents.utilities;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MiscFunc class: contains various functions used in the classes
 */
public class MiscFunc {

    /**
     * Parses the value of a text field to an integer
     *
     * @param textField The text field which has to be used for extracting a value
     * @param rand      True if a random value has to be chosen if value of text field is not an integer, false otherwise
     * @return The value of the text field
     */
    public static int parseIntTextField(JTextField textField, boolean rand) {
        int textFieldValue = -1;
        int someValue = rand ? ((int) (Math.random() * Settings.CHIP_DIVERSITY)) : 0;
        try {
            textFieldValue = Integer.parseInt(textField.getText());
        } catch (NumberFormatException ignored) {
        } finally {
            textFieldValue = ((textFieldValue >= 0) && (textFieldValue < Settings.CHIP_DIVERSITY)) ? textFieldValue : someValue;
        }
        return textFieldValue;
    }

    /**
     * Adds some style to the text pane of the panel
     *
     * @param doc the document
     */
    public static void createsStyledDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setFontSize(regular, 16);

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("boldalic", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setItalic(s, true);
    }

    /**
     * Adds style to text and sets it to the pane
     *
     * @param pane  The text pane where to set the text to
     * @param text  The text to be set to the text pane
     * @param style The style of the document
     */
    public static void addStylesToDoc(JTextPane pane, String text, String style) {
        StyledDocument doc = pane.getStyledDocument();
        MiscFunc.createsStyledDocument(doc);
        try {
            doc.remove(0, doc.getLength());
            doc.insertString(doc.getLength(), text, doc.getStyle(style));
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert text into text pane.");
        }
        pane.setEditable(false);
        pane.setOpaque(false);
    }

    /**
     * Gets the number of goal positions
     *
     * @return the number of goal positions
     */
    public static int getNumberOfGoalPositions() {
        int sum = 0;
        Point point;

        for (int x = 0; x < Settings.BOARD_WIDTH; x++) {
            for (int y = 0; y < Settings.BOARD_HEIGHT; y++) {
                point = new Point(x, y);
                if (manhattanDistance(Settings.STARTING_POSITION, point) >= Settings.MIN_GOAL_DISTANCE) {
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * calculates the manhattan distance between two points
     *
     * @param from starting point
     * @param to   destination point
     * @return the manhattan distance between two points as an integer
     */
    public static int manhattanDistance(Point from, Point to) {
        return (Math.abs(from.x - to.x) + Math.abs(from.y - to.y));
    }

    /**
     * returns all possible goal locations given a minimum distance
     *
     * @return possible goal locations
     */
    public static Map<Integer, Point> makeGoalPositionDictionary() {
        Map<Integer, Point> goalPositions = new HashMap<>();
        Point point;
        int pos = 0;

        for (int x = 0; x < Settings.BOARD_WIDTH; x++) {
            for (int y = 0; y < Settings.BOARD_HEIGHT; y++) {
                point = new Point(x, y);
                if (manhattanDistance(Settings.STARTING_POSITION, point) >= Settings.MIN_GOAL_DISTANCE) {
                    goalPositions.put(pos, point);
                    pos++;
                }
            }
        }

        return goalPositions;
    }
}
