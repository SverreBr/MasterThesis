package utilities;

import java.awt.*;
import java.util.ArrayList;

public class Settings {
//    public static Dictionary<Integer, Color> colors = new Hashtable<>();

    public static final int SCORE_GOAL = 50;
    public static final int SCORE_STEP = 10;
    public static final int SCORE_SURPLUS = 5;

    public static Color getColor(int x) {
        return switch (x) {
            case 0 -> Color.BLUE;
            case 1 -> Color.GREEN;
            case 2 -> Color.ORANGE;
            case 3 -> Color.RED;
            case 4 -> Color.YELLOW;
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
