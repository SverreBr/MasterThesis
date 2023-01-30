package utilities;

import java.awt.*;
import java.util.ArrayList;

public class Settings {
//    public static Dictionary<Integer, Color> colors = new Hashtable<>();

    /**
     * Score for reaching goal
     */
    public static final int SCORE_GOAL = 50;

    /**
     * Score subtracted when goal not reached
     */
    public static final int SCORE_STEP_SHORT = -10;

    /**
     * Score added when tokens left over
     */
    public static final int SCORE_SURPLUS = 5;

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

    public static ArrayList<Point> getGoalPositions() {
        ArrayList<Point> goalPositions = new ArrayList<>();
        goalPositions.add(new Point(0,0));
        goalPositions.add(new Point(0,1));
        goalPositions.add(new Point(0,3));
        goalPositions.add(new Point(0,4));

        goalPositions.add(new Point(1,0));
        goalPositions.add(new Point(1,4));

        goalPositions.add(new Point(3,0));
        goalPositions.add(new Point(3,4));

        goalPositions.add(new Point(4,0));
        goalPositions.add(new Point(4,1));
        goalPositions.add(new Point(4,3));
        goalPositions.add(new Point(4,4));

        return goalPositions;
    }

}
