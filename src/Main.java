import view.MainFrame;
import model.Game;

/**
 * main class for running the Colored Trails game
 */
public class Main {
    /**
     * initializes game and creates main panel
     */
    public static void main(String[] args) {
        Game game = new Game(1, 2, 0.5, 0.5, false, true);
        MainFrame main = new MainFrame(game);
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        main.requestFocus();
    }
    // TODO: add epsilon for comparisons of doubles
}
