package utilities;

// imports
import alternatingOffers.PlayerToM;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * utilities.Game class: the two players and the coloured trails board
 */
public class Game {

    /**
     * Fields
     */
    static final int BOARD_WIDTH = 5;
    static final int BOARD_HEIGHT = 5;
    static final int TOKEN_DIVERSITY = 4;
    static final int TOKENS_PER_PLAYER = 4;


    public final PlayerToM initiator;
    public final PlayerToM responder;
    public final Board board;

    /**
     * Constructor
     */
    public Game() {
        this.board = new Board(BOARD_HEIGHT, BOARD_WIDTH, TOKEN_DIVERSITY);
        this.initiator = new PlayerToM("Initiator", this);
        this.responder = new PlayerToM("Responder", this);

        initGame();
    }

    private void initGame() {
        // Distribute tokens to players
        generateAndDistributeTokens();
        assignStartingPositions();
        assignGoalPositions();
    }

    /**
     * Generate and distribute tokens for players
     */
    private void generateAndDistributeTokens() {
        List<Integer> tokensInit = new ArrayList<>();
        List<Integer> tokensResp = new ArrayList<>();

        for (int i = 0; i < TOKENS_PER_PLAYER; i++) {
            tokensInit.add((int) (Math.random() * TOKEN_DIVERSITY));
            tokensResp.add((int) (Math.random() * TOKEN_DIVERSITY));
        }
        initiator.obtainTokens(tokensInit);
        responder.obtainTokens(tokensResp);
        System.out.println("Tokens distributed.");
    }

    public void assignStartingPositions() {
        this.initiator.setStartingPosition(new Point(2, 2));
        this.responder.setStartingPosition(new Point(2, 2));
    }

    public void assignGoalPositions() {
        int randomNum;
        ArrayList<Point> goalPositions = Settings.getGoalPositions();

        randomNum = (int) (Math.random() * goalPositions.size());
        this.initiator.setGoalPosition(goalPositions.get(randomNum));

        randomNum = (int) (Math.random() * goalPositions.size());
        this.responder.setGoalPosition(goalPositions.get(randomNum));
    }

    public Dimension getBoardSize() {
        return new Dimension(board.getBoardWidth(), board.getBoardHeight());
    }
}