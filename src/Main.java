import gui.MainPanel;
import utilities.Game;

/**
 * main class for running the Colored Trails game
 */
public class Main {
    /**
     * initializes game and creates main panel
     */
    public static void main(String[] args) {
        Game game = new Game();
        MainPanel main = new MainPanel(game);
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        main.requestFocus();
    }
}
