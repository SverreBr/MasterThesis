import gui.MainFrame;
import utilities.Game;

/**
 * main class for running the Colored Trails game
 */
public class Main {
    /**
     * initializes game and creates main panel
     */
    public static void main(String[] args) {
        Game game = new Game(2, 0, 0.5, 0.5);
        MainFrame main = new MainFrame(game);
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        main.requestFocus();
    }
}
