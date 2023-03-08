package model;

/**
 * Listeners to the game model
 */
public interface GameListener {

    /**
     * Changes have been made to the game.
     */
    void gameChanged();

    /**
     * There is a new game.
     */
    default void newGame() {}

    /**
     * The field in game has changed
     */
    default void inGameChanged() {}
}
