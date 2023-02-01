package utilities;

// imports

import controller.GameListener;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * utilities.Game class: the two players and the coloured trails board
 */
public class Game {

    /**
     * the initiator agent
     */
    public Player initiator;

    public List<Integer> offerInit = null;

    /**
     * the responding agent
     */
    public Player responder;

    public List<Integer> offerResp = null;

    public String turn = "Initiator";

    /**
     * the board of the game
     */
    public Board board;

    /**
     * the listeners to the model game
     */
    private final Set<GameListener> listeners;

    public boolean inGame;

    public List<Integer> allTokens;

    /**
     * Constructor
     */
    public Game() {
        this.listeners = new HashSet<>();
        this.board = new Board();
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
        this.board.resetBoard();
        this.inGame = true;

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
        this.allTokens = new ArrayList<>();

        for (int i = 0; i < Settings.TOKENS_PER_PLAYER; i++) {
            tokensInit.add((int) (Math.random() * Settings.TOKEN_DIVERSITY));
            tokensResp.add((int) (Math.random() * Settings.TOKEN_DIVERSITY));
        }

        this.initiator.obtainTokens(tokensInit);
        this.responder.obtainTokens(tokensResp);
        this.allTokens.addAll(tokensInit);
        this.allTokens.addAll(tokensInit);
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
     *
     * @param agent the agent a goal position has to be assigned to
     */
    public void assignGoalPosition(Player agent) {
        ArrayList<Point> goalPositions = Settings.getGoalPositions(agent.getStartingPosition(), Settings.MIN_GOAL_DISTANCE,
                this.board.getBoardWidth(), this.board.getBoardHeight());

        int randomNum = (int) (Math.random() * goalPositions.size());
        agent.setGoalPosition(goalPositions.get(randomNum));
    }

    /**
     * gets the size of board
     *
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

    public void step() {
        if (!inGame) {
            System.out.println("Game is already over!");
            return;
        }

        if (turn.equals("Initiator")) {
            // turn initiator
            this.initiator.makeOffer(responder, offerResp);
        } else {
            // turn responder
            this.responder.makeOffer(responder, offerInit);
        }
        notifyListeners();
    }

    public void offerAccepted(Player p, List<Integer> offer) {
        // player p accepts offer
        inGame = false;
        List<Integer> counterOffer = new ArrayList<>(allTokens);
        for (Integer integer : offer) {
            counterOffer.remove(integer);
        }

        if (p.name.equals("Initiator")) {
            distributeTokensToPlayer(this.initiator, offer);
            distributeTokensToPlayer(this.responder, counterOffer);
        } else {
            distributeTokensToPlayer(this.initiator, counterOffer);
            distributeTokensToPlayer(this.responder, offer);
        }
    }

    public void negotiationFailed() {
        inGame = false;
        System.out.println("Negotiation failed.");
    }

    public void distributeTokensToPlayer(Player p, List<Integer> offer) {
        p.obtainTokens(offer);
    }

    public void makeOffer(Player p, List<Integer> offer) {
        if (p.name.equals("Initiator")) {
            System.out.println("Initiator makes offer");
            this.offerInit = offer;
            turn = "Responder";
        } else {
            System.out.println("Responder makes offer");
            this.offerResp = offer;
            turn = "Initiator";
        }
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
     *
     * @param listener the listener to be added to this model
     */
    public void addListener(GameListener listener) {
        this.listeners.add(listener);
    }
}