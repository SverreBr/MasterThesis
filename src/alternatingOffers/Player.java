package alternatingOffers;

import utilities.Game;

import java.awt.*;
import java.util.List;

public abstract class Player {

    /**
     * Fields
     */
    private List<Integer> tokens;
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
    public void obtainTokens(List<Integer> initTokens) {
        tokens = initTokens;
    }

    public int getNumTokens() {
        return tokens.size();
    }

    public List<Integer> getTokens() {
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
