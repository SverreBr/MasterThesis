package utilities;

// imports

import controller.GameListener;

import java.awt.*;
import java.util.*;

/**
 * utilities.Game class: the two players and the coloured trails board
 */
public class Game {

    /**
     * the initiator agent
     */
    private final Player initiator;

    /**
     * the last offer made by the initiator
     */
    private int[] offerInit;

    /**
     * the responding agent
     */
    private final Player responder;

    /**
     * the last offer made by the responder
     */
    private int[] offerResp;

    /**
     * keeps track of whose turn it is
     */
    private String turn;

    /**
     * the board of the game
     */
    private final Board board;

    /**
     * the listeners to the model game
     */
    private final Set<GameListener> listeners;

    /**
     * true if the game is running; false otherwise
     */
    private boolean inGame;

    /**
     * bin with all the chips in the game
     */
    private int[] allChipsInGame;

    /**
     * Constructor
     */
    public Game() {
        this.listeners = new HashSet<>();
        this.board = new Board(this);
        this.initiator = new PlayerToM(Settings.INITIATOR_NAME, this);
        this.responder = new PlayerToM(Settings.RESPONDER_NAME, this);

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
        this.offerInit = null;
        this.offerResp = null;
        this.turn = Settings.INITIATOR_NAME;
        this.allChipsInGame = makeNewChipBin();

        // Distribute tokens to players
        int[] newChips;
        newChips = generateNewChips();
        this.initiator.setNewChips(newChips);
        newChips = generateNewChips();
        this.responder.setNewChips(newChips);

        System.out.println("Initial all chips in game: " + Arrays.toString(allChipsInGame));

        assignStartingPositions();
        assignGoalPosition(this.initiator);
        assignGoalPosition(this.responder);
    }

    /**
     * Getter for whether the game is still active
     *
     * @return true if in game; false otherwise
     */
    public boolean isGameEnabled() {
        return this.inGame;
    }

    /**
     * Getter for board
     *
     * @return the board in the game model
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Getter for initiator agent
     *
     * @return the agent that is the initiator
     */
    public Player getInitiator() {
        return this.initiator;
    }

    /**
     * Getter for responder agent
     *
     * @return the agent that is the responder
     */
    public Player getResponder() {
        return this.responder;
    }

    /**
     * Generate and distribute tokens for the agents
     */
    private int[] generateNewChips() {
        int[] chips = makeNewChipBin();
        int newChip;

        for (int i = 0; i < Settings.CHIPS_PER_PLAYER; i++) {
            newChip = (int) (Math.random() * Settings.CHIP_DIVERSITY);
            chips[newChip] += 1;
            this.allChipsInGame[newChip] += 1;
        }
        return chips;
    }

    /**
     * Make new bin for chips.
     *
     * @return new bin for chips initialized with zero
     */
    public int[] makeNewChipBin() {
        int[] newChipBin = new int[Settings.CHIP_DIVERSITY];
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++)
            newChipBin[i] = 0;
        return newChipBin;
    }

    /**
     * assign starting positions to both agents
     */
    public void assignStartingPositions() {
        this.initiator.setStartingPosition(Settings.STARTING_POSITION);
        this.responder.setStartingPosition(Settings.STARTING_POSITION);
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
        notifyListenersNewGame();
    }

    public void step() {
        if (!inGame) {
            System.out.println("Game is already over!");
            return;
        }

        if (turn.equals(Settings.INITIATOR_NAME)) {
            // turn initiator
            this.initiator.makeOffer(responder, offerResp);
        } else {
            // turn responder
            this.responder.makeOffer(responder, offerInit);
        }
        notifyListeners();
    }

    public void offerAccepted(Player p, int[] offer) {
        // player p accepts offer
        this.inGame = false;
        int[] counterOffer = this.allChipsInGame.clone();
        for (int i = 0; i < offer.length; i++) {
            counterOffer[i] -= offer[i];
        }

        if (p.getName().equals(Settings.INITIATOR_NAME)) {
            this.initiator.setNewChips(offer);
            this.responder.setNewChips(counterOffer);
        } else {
            this.responder.setNewChips(offer);
            this.initiator.setNewChips(counterOffer);
        }
    }

    public void negotiationFailed() {
        inGame = false;
        System.out.println("Negotiation failed.");
    }

    public void makeOffer(Player p, int[] offer) {
        if (p.getName().equals(Settings.INITIATOR_NAME)) {
            System.out.println("Initiator makes offer");
            this.offerInit = offer;
            turn = Settings.RESPONDER_NAME;
        } else {
            System.out.println("Responder makes offer");
            this.offerResp = offer;
            turn = Settings.INITIATOR_NAME;
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

    protected void notifyListenersNewGame() {
        for (GameListener gameListener : this.listeners) {
            gameListener.newGame();
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

    public int[] getAllChipsInGame() {
        return this.allChipsInGame;
    }

    /**
     * Calculates the number of chips in chip array
     * @param chips the chip array
     * @return integer that represents the total number of chips.
     */
    public int calculateNumChips(int[] chips) {
        int sum = 0;
        for (int chip : chips)
            sum += chip;
        return sum;
    }
}