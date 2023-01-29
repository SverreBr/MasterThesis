package utilities;

import java.awt.*;

public class Board {

    private final int[][] board;
    private final int boardHeight;
    private final int boardWidth;
    private final Settings settings;

    /**
     * Constructs a random square board setting according to the specified parameters
     *
     * @param boardWidth      size of the (square) board in number of tiles
     * @param boardHeight     size of the board in number of tiles
     * @param tokenDiversity  number of different colors of tiles and chips
     */
    public Board(int boardHeight, int boardWidth, int tokenDiversity, Settings settings) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        board = new int[boardHeight][boardWidth];
        initBoard(tokenDiversity);
        this.settings = settings;
    }

    private void initBoard(int tokenDiversity) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = (int) (Math.random() * tokenDiversity);
            }
        }
    }

    public int getSquareNumber(int x, int y) {
        return board[x][y];
    }

    public Color getSquareColor(int x, int y) {
        return settings.getColor(board[x][y]);
    }

    public int getHeight() {
        return boardHeight;
    }

    public int getWidth() {
        return boardWidth;
    }
}
