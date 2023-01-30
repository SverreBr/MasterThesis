package alternatingOffers;

import utilities.Game;
import utilities.Settings;

import java.awt.*;
import java.util.ArrayList;
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

    public void resetPlayer() {
        tokens = new ArrayList<Integer>();
        startingPosition = null;
        goalPosition = null;
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
