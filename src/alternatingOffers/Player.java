package alternatingOffers;

import utilities.Game;

import java.awt.*;

public abstract class Player {

    /**
     * Fields
     */
    private int[] tokens;
    public final String name;
    public final Game game;

    public Point startingPosition;
    public Point goalPosition;

    public Player (String namePlayer, Game game) {
        name = namePlayer;
        this.game = game;
    }

    /**
     * Give the player tokens
     */
    public void obtainTokens(int [] initTokens) {
        tokens = initTokens;
    }

    public int getNumTokens() {
        return tokens.length;
    }

    public int[] getTokens() {
        return tokens;
    }

//    public int calculateCurrentPoints() {
//
//    }

    public void setStartingPosition(Point startingPosition) {
        this.startingPosition = startingPosition;
    }

    public Point getStartingPosition() {
        return startingPosition;
    }

    public void setGoalPosition(Point goalPosition) {
        this.goalPosition = goalPosition;
    }

    public Point getGoalPosition() {
        return goalPosition;
    }

}
