package utilities;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;

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
    public static final int TOKEN_DIVERSITY = 3;

    /**
     * number of tokens an agent obtains
     */
    public static final int TOKENS_PER_PLAYER = 4;

    /**
     * minimum (manhattan) distance from start location to goal location where the goal can be placed
     */
    public static final int MIN_GOAL_DISTANCE = 3;


    public static final int BUTTON_PANEL_WIDTH = 300;
    public static final int AGENT_PANEL_HEIGHT = 500;
    public static final int AGENT_TEXT_HEIGHT = 200;

//    int BUTTON_PANEL_HEIGHT = 0;
    public static final int MAIN_PANEL_SIZE = 750;

    public static final String START_LOCATION_SYMBOL = "X";
    public static final String START_LOCATION_SYMBOL_INITIATOR = "Xi";
    public static final String START_LOCATION_SYMBOL_RESPONDER = "Xr";
    public static final String GOAL_LOCATION_SYMBOL = "G";
    public static final String GOAL_LOCATION_SYMBOL_INITIATOR = "Gi";
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
    public static ArrayList<Point> getGoalPositions(Point startLoc, int minDistance, int maxX, int maxY) {
        ArrayList<Point> goalPositions = new ArrayList<>();
        Point point;

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                point = new Point(x, y);
                if (manhattanDistance(startLoc, point) >= minDistance) {
                    goalPositions.add(point);
                }
            }
        }

        return goalPositions;
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
     * Adds some style to the text pane of the panel
     *
     * @param doc the document
     */
    public static void addStylesToDocument(StyledDocument doc) {
        // Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setFontSize(regular, 16);

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
    }

    public static Color getBackGroundColor() {
        return Color.decode("#fffcdf");
    }
}
