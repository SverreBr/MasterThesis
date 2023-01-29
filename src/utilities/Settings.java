package utilities;

import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Settings {
    public static Dictionary<Integer, Color> colors = new Hashtable<Integer, Color>();

    public Settings() {
        makeColorDictionary();
    }

    private static void makeColorDictionary() {
        colors.put(0, Color.BLUE);
        colors.put(1, Color.GREEN);
        colors.put(2, Color.ORANGE);
        colors.put(3, Color.RED);
        colors.put(4, Color.YELLOW);
    }

    public Color getColor(int x) {
        return colors.get(x);
    }
}
