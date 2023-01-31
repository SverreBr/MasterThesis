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
     * the height of the board (y); the first entry of the board
     */
    private final int boardHeight;

    /**
     * the width of the board (x); the second entry of the board
     */
    private final int boardWidth;

    /**
     * Constructs a random square board setting according to the specified parameters
     *
     * @param boardWidth     size of the (square) board in number of tiles
     * @param boardHeight    size of the board in number of tiles
     * @param tokenDiversity number of different colors of tiles and chips
     */
    public Board(int boardHeight, int boardWidth, int tokenDiversity) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        board = new int[boardHeight][boardWidth];
        initBoard(tokenDiversity);
    }

    /**
     * resets the board and initializes a new one.
     *
     * @param tokenDiversity the number of different colors used to construct the board
     */
    public void resetBoard(int tokenDiversity) {
        this.initBoard(tokenDiversity);
    }

    /**
     * Initializes the board, that is, we give numbers to each tile corresponding to a color.
     *
     * @param tokenDiversity the number of different colors to be used in the board
     */
    private void initBoard(int tokenDiversity) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = (int) (Math.random() * tokenDiversity);
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

    /**
     * Calculates the shortest manhattan distance from a particular location to the goal location
     *
     * @param currentLoc the current location
     * @param goalLoc    the goal location
     * @return the manhattan distance from current location to goal location
     */
    public int distanceToGoal(Point currentLoc, Point goalLoc) {
        return Math.abs(currentLoc.x - goalLoc.x) + Math.abs(currentLoc.y - goalLoc.y);
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
     * @param tokens  the tokens
     * @param goalLoc goal location
     * @return the score corresponding to this tile
     */
    public int calculateTileScore(Point currLoc, List<Integer> tokens, Point goalLoc) {
        int score = 0;
        if (currLoc.equals(goalLoc)) {
            score += Settings.SCORE_GOAL;
        } else {
            score += Settings.SCORE_STEP_SHORT * distanceToGoal(currLoc, goalLoc);
        }
        score += Settings.SCORE_SURPLUS * tokens.size();
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
}
