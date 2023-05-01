package lyingAgents;

import lyingAgents.view.MainFrame;
import lyingAgents.model.Game;

/**
 * main class for running the Colored Trails game
 */
public class Main {
    /**
     * initializes game and creates main panel
     */
    public static void main(String[] args) {
        Game game = new Game(2, 2, 0.5, 0.5, true, true, true, true);
        MainFrame main = new MainFrame(game);
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        main.requestFocus();
    }
}
