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
     * number of tiles as the width of the board
     */
    static private final int BOARD_WIDTH = 5;

    /**
     * number of tiles as the height of the board
     */
    static private final int BOARD_HEIGHT = 5;

    /**
     * number of different colors as tiles of the board
     */
    static private final int TOKEN_DIVERSITY = 3;

    /**
     * number of tokens an agent obtains
     */
    static private final int TOKENS_PER_PLAYER = 4;

    /**
     * minimum (manhattan) distance from start location to goal location where the goal can be placed
     */
    static private final int MIN_GOAL_DISTANCE = 3;

    /**
     * the initiator agent
     */
    public PlayerToM initiator;

    /**
     * the responding agent
     */
    public PlayerToM responder;

    /**
     * the board of the game
     */
    public Board board;

    /**
     * the listeners to the model game
     */
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

    /**
     * Initializes the game. Resets agents, resets the board, generates and distributes new tokens, and assigns starting
     * and goal positions to the agents.
     */
    private void initGame() {
        this.initiator.resetPlayer();
        this.responder.resetPlayer();
        this.board.resetBoard(TOKEN_DIVERSITY);

        // Distribute tokens to players
        generateAndDistributeTokens();
        assignStartingPositions();
        assignGoalPosition(initiator);
        assignGoalPosition(responder);
    }

    /**
     * Generate and distribute tokens for the agents
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

    /**
     * assign starting positions to both agents
     */
    public void assignStartingPositions() {
        this.initiator.setStartingPosition(new Point(2, 2));
        this.responder.setStartingPosition(new Point(2, 2));
    }

    /**
     * assigns a goal position to the agent
     * @param agent the agent a goal position has to be assigned to
     */
    public void assignGoalPosition(PlayerToM agent) {
        ArrayList<Point> goalPositions = Settings.getGoalPositions(agent.getStartingPosition(), MIN_GOAL_DISTANCE,
                this.board.getBoardWidth(), this.board.getBoardHeight());

        int randomNum = (int) (Math.random() * goalPositions.size());
        agent.setGoalPosition(goalPositions.get(randomNum));
    }

    /**
     * gets the size of board
     * @return a dimension with the width and height of the board
     */
    public Dimension getBoardSize() {
        return new Dimension(this.board.getBoardWidth(), this.board.getBoardHeight());
    }

    /**
     * resets the board to a new initialization
     */
    public void reset() {
        initGame();
        notifyListeners();
    }

    /**
     * when a change occurs to the game, the listeners are notified with this method
     */
    protected void notifyListeners() {
        System.out.println("\n------\n");
        for (GameListener gameListener : this.listeners) {
            gameListener.gameChanged();
        }
    }

    /**
     * adds a listener to this model
     * @param listener the listener to be added to this model
     */
    public void addListener(GameListener listener) {
        this.listeners.add(listener);
    }
}