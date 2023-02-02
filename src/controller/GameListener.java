package controller;

/**
 * Listeners to the game model
 */
public interface GameListener {

    /**
     * Changes have been made to the game.
     */
    void gameChanged();

    default void newGame() {}
}
