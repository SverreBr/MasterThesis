package alternatingOffers;

import utilities.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Player: abstract class for players
 */
public abstract class Player {

    /**
     * list of integers that represent the colored chips
     */
    private List<Integer> tokens;

    /**
     * Name of the agent
     */
    public final String name;

    /**
     * Model of the game
     */
    public final Game game;

    /**
     * Starting position of the agent
     */
    public Point startingPosition;

    /**
     * Goal position of the agent
     */
    public Point goalPosition;

    /**
     * Constructor
     *
     * @param namePlayer name of the agent
     * @param game       model of the game
     */
    public Player(String namePlayer, Game game) {
        name = namePlayer;
        this.game = game;
    }

    /**
     * Resets the agent. Removes all colored chips, and sets the starting and goal position to null
     */
    public void resetPlayer() {
        tokens = new ArrayList<>();
        startingPosition = null;
        goalPosition = null;
    }

    /**
     * Give the player colored chips
     */
    public void obtainTokens(List<Integer> initTokens) {
        tokens = initTokens;
    }

    /**
     * Get the number of chips
     *
     * @return number of chips
     */
    public int getNumTokens() {
        return tokens.size();
    }

    /**
     * Get the chips of the agent
     *
     * @return a list of integers that represent the tokens
     */
    public List<Integer> getTokens() {
        return tokens;
    }

    /**
     * Sets the starting position of the agent
     *
     * @param startingPosition the starting position of the agent
     */
    public void setStartingPosition(Point startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Gets the starting position of the agent
     *
     * @return the starting position of the agent
     */
    public Point getStartingPosition() {
        return startingPosition;
    }

    /**
     * Sets the goal position of the agent
     *
     * @param goalPosition the goal position of the agent
     */
    public void setGoalPosition(Point goalPosition) {
        this.goalPosition = goalPosition;
    }

    /**
     * Gets the goal position of the agent
     *
     * @return the goal position of the agent
     */
    public Point getGoalPosition() {
        return goalPosition;
    }

    /**
     * Calculates the score that an agent obtains when starting on the currLoc and having goalLoc as goal location
     * with tokens as his colored chips
     *
     * @param currLoc starting location
     * @param tokens  the colored chips
     * @param goalLoc goal location
     * @return the score as an integer
     */
    public int calculateScore(Point currLoc, List<Integer> tokens, Point goalLoc) {
        // Calculate current score.
        int currScore = game.board.calculateTileScore(currLoc, tokens, goalLoc);

        if (currLoc.equals(goalLoc) || (tokens.size() == 0)) {
            // Goal location reached or no possible moves anymore
            return currScore;
        }

        int tileColor, highestScore = currScore;
        Point newLoc;
        List<Integer> newTokens;
        List<Point> possibleMoves = game.board.getPossibleMoves();
        for (Point move : possibleMoves) {
            newLoc = new Point(currLoc.x + move.x, currLoc.y + move.y);
            if ((0 <= newLoc.x) && (newLoc.x < game.board.getBoardWidth()) &&
                    (0 <= newLoc.y) && (newLoc.y < game.board.getBoardHeight())) {
                tileColor = game.board.getTileColorNumber(newLoc);
                if (tokens.contains(tileColor)) {
                    // Move is allowed
                    newTokens = new ArrayList<>(tokens);
                    newTokens.remove(Integer.valueOf(tileColor));
                    highestScore = Math.max(highestScore, calculateScore(newLoc, newTokens, goalLoc));
                }
            }
        }
        return highestScore;
    }
}
