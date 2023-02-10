package utilities;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.*;

/**
 * class that contains the settings of the game
 */
public class Settings {

    /**
     * Score for reaching goal
     */
    public static final int SCORE_GOAL = 50;

    /**
     * Score subtracted when goal not reached
     */
    public static final int SCORE_STEP = 10;

    /**
     * Score added when tokens left over
     */
    public static final int SCORE_SURPLUS = 5;

    /**
     * number of tiles as the width of the board
     */
    public static final int BOARD_WIDTH = 5;

    /**
     * number of tiles as the height of the board
     */
    public static final int BOARD_HEIGHT = 5;

    /**
     * number of different colors as tiles of the board
     */
    public static final int CHIP_DIVERSITY = 4;

    /**
     * number of tokens an agent obtains
     */
    public static final int CHIPS_PER_PLAYER = 4;

    /**
     * minimum (manhattan) distance from start location to goal location where the goal can be placed
     */
    public static final int MIN_GOAL_DISTANCE = 3;

    /**
     * determines the width of the button panel
     */
    public static final int BUTTON_PANEL_WIDTH = 300;

    /**
     * determines the height of the agent panel
     */
    public static final int AGENT_PANEL_HEIGHT = 570;

    /**
     * determines the height of the text panel in the agent panel
     */
    public static final int AGENT_TEXT_HEIGHT = 290;

    /**
     * Size of the main panel
     */
    public static final int MAIN_PANEL_SIZE = 800;

    /**
     * The starting position of each agent
     */
    public static final Point STARTING_POSITION = new Point(2, 2);

    /**
     * The name of the initiator
     */
    public static final String INITIATOR_NAME = "Initiator";

    /**
     * The name of the responder
     */
    public static final String RESPONDER_NAME = "Responder";

    /**
     * The symbol used to indicate the start
     */
    public static final String START_LOCATION_SYMBOL = "X";

    /**
     * The symbol used to indicate the goal position if for both agents equal
     */
    public static final String GOAL_LOCATION_SYMBOL = "G";

    /**
     * The symbol used to indicate the goal position of the initiator
     */
    public static final String GOAL_LOCATION_SYMBOL_INITIATOR = "Gi";

    /**
     * The symbol used to indicate the goal position of the responder
     */
    public static final String GOAL_LOCATION_SYMBOL_RESPONDER = "Gr";

    /**
     * Returns a color given a certain integer
     *
     * @param x the integer number
     * @return the color corresponding to the integer
     */
    public static Color getColor(int x) {
        return switch (x) {
            case 0 -> Color.decode("#0072b2");
            case 1 -> Color.decode("#009e73");
            case 2 -> Color.decode("#d55e00");
            case 3 -> Color.decode("#cc79a7");
            case 4 -> Color.decode("#f0e442");
            default -> Color.BLACK;
        };
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

        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                point = new Point(x, y);
                if (manhattanDistance(STARTING_POSITION, point) >= MIN_GOAL_DISTANCE) {
                    goalPositions.put(pos, point);
                    pos++;
                }
            }
        }

        return goalPositions;
    }

//    public static int getNumberOfGoalPositions() {
//        int numGoalPos = 0;
//        Point point;
//        for (int x = 0; x < BOARD_WIDTH; x++) {
//            for (int y = 0; y < BOARD_HEIGHT; y++) {
//                point = new Point(x,y);
//                if (manhattanDistance(STARTING_POSITION, point) >= MIN_GOAL_DISTANCE) {
//                    numGoalPos += 1;
//                }
//            }
//        }
//        return numGoalPos;
//    }

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
     * Adds some style to the text pane of the panel
     *
     * @param doc the document
     */
    public static void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setFontSize(regular, 16);

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
    }

    /**
     * Gets the background color of the simulation
     *
     * @return the color of the background for the simulation
     */
    public static Color getBackGroundColor() {
        return Color.decode("#fffcdf");
    }
}
