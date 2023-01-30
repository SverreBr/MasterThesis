package utilities;

// imports
import alternatingOffers.PlayerToM;
import controller.GameListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * utilities.Game class: the two players and the coloured trails board
 */
public class Game {

    /**
     * Fields
     */
    static private final int BOARD_WIDTH = 5;
    static private final int BOARD_HEIGHT = 5;
    static private final int TOKEN_DIVERSITY = 3;
    static private final int TOKENS_PER_PLAYER = 4;


    public PlayerToM initiator;
    public PlayerToM responder;
    public Board board;

    private final Set<GameListener> listeners;

    /**
     * Constructor
     */
    public Game() {
        this.listeners = new HashSet<>();
        this.board = new Board(BOARD_HEIGHT, BOARD_WIDTH, TOKEN_DIVERSITY);
        this.initiator = new PlayerToM("Initiator", this);
        this.responder = new PlayerToM("Responder", this);

        initGame();
    }

    private void initGame() {
        this.initiator.resetPlayer();
        this.responder.resetPlayer();
        this.board.resetBoard(TOKEN_DIVERSITY);

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
        this.initiator.obtainTokens(tokensInit);
        this.responder.obtainTokens(tokensResp);
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
        return new Dimension(this.board.getBoardWidth(), this.board.getBoardHeight());
    }

    public void reset() {
        initGame();
        notifyListeners();
    }

    protected void notifyListeners() {
        System.out.println("\n------\n");
        for (GameListener gameListener : this.listeners) {
            gameListener.gameChanged();
        }
    }

    public void addListener(GameListener listener) {
        this.listeners.add(listener);
    }
}