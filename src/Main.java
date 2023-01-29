import gui.MainPanel;
import utilities.Game;

/**
 * Main class for running the Colored Trails game
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        MainPanel main = new MainPanel(game);
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        main.requestFocus();
    }
}
