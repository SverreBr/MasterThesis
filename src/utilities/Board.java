package utilities;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int[][] board;
    private final int boardHeight;
    private final int boardWidth;

    /**
     * Constructs a random square board setting according to the specified parameters
     *
     * @param boardWidth      size of the (square) board in number of tiles
     * @param boardHeight     size of the board in number of tiles
     * @param tokenDiversity  number of different colors of tiles and chips
     */
    public Board(int boardHeight, int boardWidth, int tokenDiversity) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        board = new int[boardHeight][boardWidth];
        initBoard(tokenDiversity);
    }

    private void initBoard(int tokenDiversity) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = (int) (Math.random() * tokenDiversity);
            }
        }
    }

    public int getTileColorNumber(int x, int y) {
        return board[x][y];
    }

    /**
     * Get color of the (x,y) square of the board
     * @param x x-location
     * @param y y-location
     * @return The color corresponding to the square on the board
     */
    public Color getTileColor(int x, int y) {
        return Settings.getColor(board[x][y]);
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Calculates the shortest manhattan distance from a particular location to the goal location
     * @param currentLoc    the current location
     * @param goalLoc       the goal location
     * @return              the manhattan distance from current location to goal location
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

//    public int calculateScore(Point currLoc, List<Integer> tokens, Point goalLoc) {
//
//        if (currLoc.equals(goalLoc)) {
//            // Goal location reached
//            return (Settings.SCORE_GOAL + Settings.SCORE_SURPLUS * tokens.size());
//        }
//
//        if (tokens.size() == 0) {
//            // No possible moves anymore
//            return (Settings.SCORE_STEP_SHORT * distanceToGoal(currLoc, goalLoc));
//        }
//
//        int newX, newY, tileColor;
//        List<Point> possibleMoves = getPossibleMoves();
//        for (Point move : possibleMoves) {
//            newX = currLoc.x + move.x;
//            newY = currLoc.y + move.y;
//            if ((0 <= newX) && (newX < boardWidth) && (0 <= newY) && (newY < boardHeight)) {
//                tileColor = getTileColorNumber(newX, newY);
//                if (tokens.contains(tileColor)) {
//                    // Move is allowed
//                    newPoints = points
//                    someFunction()
//                }
//                someFunction(new Point(newX, newY), points)
//            }
//        }
//    }

    private List<Point> getPossibleMoves() {
        return Arrays.asList(
                new Point(-1,-1),
                new Point(-1, 1),
                new Point(1, -1),
                new Point (1, 1)
        );
    }
}
