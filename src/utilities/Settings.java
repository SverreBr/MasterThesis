package utilities;

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
    public static final int SCORE_STEP_SHORT = -10;

    /**
     * Score added when tokens left over
     */
    public static final int SCORE_SURPLUS = 5;

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
}
