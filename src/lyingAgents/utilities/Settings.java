package lyingAgents.utilities;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * class that contains the settings of the game
 */
public class Settings {

    /**
     * Score for reaching goal
     */
    public static final int SCORE_GOAL = 500;

    /**
     * Score subtracted when goal not reached
     */
    public static final int SCORE_STEP = 100;

    /**
     * Score added when tokens left over
     */
    public static final int SCORE_SURPLUS = 50;

    /**
     * Score added for every offer made in the game
     */
    public static final int SCORE_NEGOTIATION_STEP = -1;

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
    public static final int CHIP_DIVERSITY = 5;

    /**
     * number of tokens an agent obtains
     */
    public static final int CHIPS_PER_PLAYER = 4;

    /**
     * minimum (manhattan) distance from start location to goal location where the goal can be placed
     */
    public static final int MIN_GOAL_DISTANCE = 3;

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
     * Message an agent provides when it terminates negotiation
     */
    public static final String TERMINATE_NEGOTIATION_MESSAGE = "I end negotiation here";

    /**
     * Message an agent provides when it accepts an offer
     */
    public static final String ACCEPT_OFFER_MESSAGE = "I accept your offer.";

    /**
     * the size of the array in which agents can save their beliefs
     */
    public static final int SAVE_NUMBER = 3;

    /**
     * The error for comparing double values
     */
    public static final double EPSILON = 1e-9;

    /**
     * The maximum number of offers after which negotiation is terminated
     */
    public static final int MAX_NUMBER_OFFERS = 50;

    /**
     * Shown when a general exception occurs
     */
    public static final String GENERAL_EXCEPTION = "Hmm... I don't know what went wrong, but don't do that again please.";

    /**
     * A formatter for printing doubles
     */
    public static final DecimalFormat PRINT_DF = new DecimalFormat("####0.0000");


    /**
     * Returns a color given a certain integer
     *
     * @param x the integer number
     * @return the color corresponding to the integer
     */
    public static Color getColor(int x) {
        Color color;
        switch (x) {
            case 0:
                color = Color.decode("#6248DA");
                break;
            case 1:
                color = Color.decode("#DC267F");
                break;
            case 2:
                color = Color.decode("#FE6100");
                break;
            case 3:
                color = Color.decode("#96B1F7");
                break;
            case 4:
                color = Color.decode("#fccc60");
                break;
            default:
                color = Color.BLACK;
        }
        return color;
    }

    /**
     * get the possible moves on the board
     *
     * @return a list of possible moves as points
     */
    public static List<Point> getPossibleMoves() {
        return Arrays.asList(
                new Point(-1, 0),
                new Point(1, 0),
                new Point(0, -1),
                new Point(0, 1)
        );
    }
}
