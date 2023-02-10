package utilities;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Board class: the game board of the colored trails
 */
public class Board {

    /**
     * the board as a matrix containing numbers as colors
     */
    private final int[][] board;

    /**
     * the game model
     */
    private final Game game;

    /**
     * the height of the board (y); the first entry of the board
     */
    private final int boardHeight;

    /**
     * the width of the board (x); the second entry of the board
     */
    private final int boardWidth;

    /**
     * Constructs a random square board setting according to the specified parameters
     */
    public Board(Game game) {
        this.boardHeight = Settings.BOARD_HEIGHT;
        this.boardWidth = Settings.BOARD_WIDTH;
        this.game = game;
        this.board = new int[boardHeight][boardWidth];
        initBoard();
    }

    /**
     * resets the board and initializes a new one.
     */
    public void resetBoard() {
        this.initBoard();
    }

    /**
     * Initializes the board, that is, we give numbers to each tile corresponding to a color.
     */
    private void initBoard() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = (int) (Math.random() * Settings.CHIP_DIVERSITY);
            }
        }
    }

    /**
     * gets the number (corresponding to a color) of a tile of the board
     *
     * @param point the coordinates of the tile
     * @return the number that corresponds to the color of that tile
     */
    public int getTileColorNumber(Point point) {
        return board[point.y][point.x];
    }

    /**
     * Get color of the (x,y) square of the board
     *
     * @return The color corresponding to the square on the board
     */
    public Color getTileColor(Point point) {
        return Settings.getColor(board[point.y][point.x]);
    }

    /**
     * gets the height of the board
     *
     * @return the board height
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * gets the width of the board
     *
     * @return the board width
     */
    public int getBoardWidth() {
        return boardWidth;
    }

//    public List<int[]> getShortestPathGivenTokens(int[] currentLoc, int[] goalLoc, int[] tokens) {
//        int dist = distanceToGoal(currentLoc, goalLoc);
//        int initPoints = tokens.length * Settings.SCORE_SURPLUS - dist * Settings.SCORE_STEP;
//
//        List<int[]> shortestPath = new ArrayList<>();
//        return shortestPath;
//    }

    /**
     * calculates the score from a current tile to a goal tile given some tokens
     *
     * @param currLoc current location
     * @param chips   the colored chips
     * @param goalLoc goal location
     * @return the score corresponding to this tile
     */
    private int calculateTileScore(Point currLoc, int[] chips, Point startLoc, Point goalLoc) {
        int score = 0, stepsTowardsGoal;
        stepsTowardsGoal = Settings.manhattanDistance(startLoc, goalLoc) - Settings.manhattanDistance(currLoc, goalLoc);

        if (currLoc.equals(goalLoc))
            score += Settings.SCORE_GOAL;
        score += Settings.SCORE_STEP * Math.max(0, stepsTowardsGoal);
        score += Settings.SCORE_SURPLUS * this.game.calculateNumChips(chips);
        return score;
    }

    /**
     * get the possible moves on the board
     *
     * @return a list of possible moves as points
     */
    public List<Point> getPossibleMoves() {
        return Arrays.asList(
                new Point(-1, 0),
                new Point(1, 0),
                new Point(0, -1),
                new Point(0, 1)
        );
    }

    /**
     * Calculates the score that an agent obtains when starting on the currLoc and having goalLoc as goal location
     * with chips as his colored chips
     *
     * @param currLoc starting location
     * @param chips   the colored chips
     * @param goalLoc goal location
     * @return the score as an integer
     */
    public int calculateScore(Point currLoc, int[] chips, Point startLoc, Point goalLoc) {
        // Calculate current score.
        int currScore = this.calculateTileScore(currLoc, chips, startLoc, goalLoc);

        if (currLoc.equals(goalLoc) || (this.game.calculateNumChips(chips) == 0)) { // Could be optimised
            // Goal location reached or no chips to move anymore
            return currScore;
        }

        int tileColor, highestScore = currScore;
        Point newLoc;
        int[] newTokens;
        List<Point> possibleMoves = this.getPossibleMoves();
        for (Point move : possibleMoves) {
            newLoc = new Point(currLoc.x + move.x, currLoc.y + move.y);
            if ((0 <= newLoc.x) && (newLoc.x < boardWidth) &&
                    (0 <= newLoc.y) && (newLoc.y < boardHeight)) {
                tileColor = this.getTileColorNumber(newLoc);
                if (chips[tileColor] > 0) {
                    // Move is allowed
                    newTokens = chips.clone();
                    newTokens[tileColor] -= 1;
                    highestScore = Math.max(highestScore, calculateScore(newLoc, newTokens, startLoc, goalLoc));
                }
            }
        }
        return highestScore;
    }
}
