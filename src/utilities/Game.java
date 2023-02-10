package utilities;

import controller.GameListener;

import java.awt.*;
import java.util.*;

/**
 * utilities.Game class: the two players and the coloured trails board
 */
public class Game {

    /**
     * bin with all the chips in the game
     */
    private int[] binMaxChips;

    /**
     * utility functions (utility as a function of offer) for each possible goal location
     */
    private int[][] utilityFunctions;

    private int[] flipArray;

    private final int[] chipSets = new int[2];

    private final int[] goalPositions = new int[2];

    private final Map<Integer, Point> goalPositionsDict;

    private final int nrGoalPositions;

    /**
     * the initiator agent
     */
    private final Player initiator;

    /**
     * the last offer made
     */
    private int newOffer;

    /**
     * the responding agent
     */
    private final Player responder;

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

    private boolean simulationOn = true;

    /**
     * Constructor
     */
    public Game() {
        this.listeners = new HashSet<>();
        this.board = new Board(this);
        this.goalPositionsDict = Settings.makeGoalPositionDictionary();
        this.nrGoalPositions = this.goalPositionsDict.size();
        this.initiator = new PlayerToM(Settings.INITIATOR_NAME, this, 0);
        this.responder = new PlayerToM(Settings.RESPONDER_NAME, this, 0);
        initNewGame();
    }

    private void initNewGame() {
        generateNewNegotiationSetting();

        int initIdx = getPlayerIdx(Settings.INITIATOR_NAME);
        int respIdx = getPlayerIdx(Settings.RESPONDER_NAME);
        this.initiator.reset(chipSets[initIdx], chipSets[respIdx], utilityFunctions[goalPositions[initIdx]]);
        this.responder.reset(chipSets[respIdx], chipSets[initIdx], utilityFunctions[goalPositions[respIdx]]);
    }

    private void generateNewNegotiationSetting() {
        this.inGame = true;
        this.newOffer = -1;
        this.turn = Settings.INITIATOR_NAME;

        // Distribute tokens to players
        int[] chipsInit = generateNewChips();  // will be stored in chipSets[0] as index
        int[] chipsResp = generateNewChips();  // will be stored in chipSets[1] as index
        calculateSetting(chipsInit, chipsResp);
        assignGoalPositions();
    }

    /**
     * resets the board to a new initialization
     */
    public void reset() {
        this.board.resetBoard();
        this.initNewGame();
        if (simulationOn)
            notifyListenersNewGame();
    }

    public void newRound() {
        this.board.resetBoard();
        generateNewNegotiationSetting();

        int initIdx = getPlayerIdx(Settings.INITIATOR_NAME);
        int respIdx = getPlayerIdx(Settings.RESPONDER_NAME);
        this.initiator.initNewRound(chipSets[initIdx], chipSets[respIdx], utilityFunctions[goalPositions[initIdx]]);
        this.responder.initNewRound(chipSets[respIdx], chipSets[initIdx], utilityFunctions[goalPositions[respIdx]]);
        if (simulationOn)
            notifyListenersNewGame();
    }


    public void calculateSetting(int[] chipsInit, int[] chipsResp) {
//        int[][][] locationScoreMatrix;
        int numIndexCodes;
        this.binMaxChips = Chips.makeNewChipBin();

        numIndexCodes = 1;
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            this.binMaxChips[i] = chipsInit[i] + chipsResp[i];
            numIndexCodes *= (this.binMaxChips[i] + 1); // to account for 0 chips in that bin
        }
        chipSets[0] = Chips.getIndex(chipsInit, this.binMaxChips);
        chipSets[1] = Chips.getIndex(chipsResp, this.binMaxChips);
//        locationScoreMatrix = new int[Settings.BOARD_HEIGHT][Settings.BOARD_HEIGHT][numIndexCodes];
//        calculateLocationScoreMatrix(locationScoreMatrix);

        int pos;
        Point goalPosition;
        utilityFunctions = new int[this.nrGoalPositions][numIndexCodes];
        for (Map.Entry<Integer, Point> entry : goalPositionsDict.entrySet()) {
            pos = entry.getKey();
            goalPosition = entry.getValue();
            calcUtilityFunction(utilityFunctions[pos], goalPosition);
        }
        makeNewFlipArrayOffer(numIndexCodes);
    }

    private void makeNewFlipArrayOffer(int numIndexCodes) {
        flipArray = new int[numIndexCodes];
        for (int offer = 0; offer < numIndexCodes; offer++) {
            flipArray[offer] = Chips.invert(offer, binMaxChips);
        }
    }

    public void calcUtilityFunction(int[] utilityFunction, Point goalPosition) {
        // CHECK IF THIS IS CORRECT!!!
        int[] offer;

        for (int offerIdx = 0; offerIdx < utilityFunction.length; offerIdx++) {
            offer = Chips.getBins(offerIdx, binMaxChips);
            utilityFunction[offerIdx] = board.calculateScore(Settings.STARTING_POSITION, offer, Settings.STARTING_POSITION, goalPosition);
        }
    }

    public Point getGoalPositionPointPlayer(String player) {
        int playerIdx = getPlayerIdx(player);
        return goalPositionsDict.get(goalPositions[playerIdx]);
    }

    /**
     * Returns the given offer from the perspective of the other agent
     *
     * @param offer offer from the perspective of agent i
     * @return offer from the perspective of agent j
     */
    public int flipOffer(int offer) {
        if (offer < 0) offer = 0;
        return flipArray[offer];
    }

    public int getPlayerIdx(String p) {
        int playerIdx = p.equals(Settings.INITIATOR_NAME) ? 0 : 1;
        return playerIdx;
    }

    /**
     * Returns the utility table (offer -> utility) for goal location i
     *
     * @param i goal location
     * @return utility table (offer -> utility) for goal location i
     */
    public int[] getUtilityFunction(int i) {
        return utilityFunctions[i];
    }

    public int getNumberOfGoalPositions() {
        return this.nrGoalPositions;
    }

    /**
     * Getter for whether the game is still active
     *
     * @return true if in game; false otherwise
     */
    public boolean isGameDisabled() {
        return !this.inGame;
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
        int[] chips = Chips.makeNewChipBin();
        int newChip;

        for (int i = 0; i < Settings.CHIPS_PER_PLAYER; i++) {
            newChip = (int) (Math.random() * Settings.CHIP_DIVERSITY);
            chips[newChip] += 1;
        }
        return chips;
    }

    /**
     * assigns a goal position to each agent
     */
    public void assignGoalPositions() {
        for (int i = 0; i < goalPositions.length; i++) {
            goalPositions[i] = (int) (Math.random() * goalPositionsDict.size());
        }
    }

    /**
     * gets the size of board
     *
     * @return a dimension with the width and height of the board
     */
    public Dimension getBoardSize() {
        return new Dimension(this.board.getBoardWidth(), this.board.getBoardHeight());
    }


    public void playTillEnd() {
        int i = 0;
        while (i < 100 && inGame) {
            step();
            i++;
        }
        if (i >= 100) {
            System.out.println("one hundred steps performed.");
        }
    }
    public void step() {
        int tmpNewOffer, flippedOffer;
        boolean negotiationEnds = false;

        if (!inGame) {
            System.out.println("Game is already over!");
            return;
        }

        if (turn.equals(Settings.INITIATOR_NAME)) {
            // turn initiator
            System.out.println("Initiator takes turn");
            tmpNewOffer = this.initiator.makeOffer(newOffer);

            // negotiation ends when agent offers original distribution
            if (tmpNewOffer == this.responder.chips) negotiationEnds = true;
        } else {
            // turn responder
            System.out.println("Responder takes turn");
            tmpNewOffer = this.responder.makeOffer(newOffer);

            if (tmpNewOffer == this.initiator.chips) negotiationEnds = true;
        }
        System.out.println("Offers " + Arrays.toString(Chips.getBins(tmpNewOffer, binMaxChips)));

        flippedOffer = flipOffer(tmpNewOffer);
        if (negotiationEnds) { // Negotiation terminated
            negotiationTerminates();
        } else if (flippedOffer == newOffer) { // Offer is accepted
            offerAccepted(tmpNewOffer);
        } else { // Negotiation continues
            newOffer = tmpNewOffer;
            addOfferMessage();
            switchTurn();
        }

        if (simulationOn)
            notifyListeners();
        // TODO: When is an offer accepted, what is returned?
        // TODO: How does one retreat from negotiations?
    }

    public void addOfferMessage() {
        int offerSelf = flipOffer(newOffer);
        String message = "I offer you: " + Arrays.toString(Chips.getBins(newOffer, binMaxChips)) + "; (" + offerSelf + "-" + newOffer + ")";
        if (turn.equals(Settings.INITIATOR_NAME)) {
            this.initiator.addMessage(message);
        } else {
            this.responder.addMessage(message);
        }
    }

    public void switchTurn() {
        turn = turn.equals(Settings.INITIATOR_NAME) ? Settings.RESPONDER_NAME : Settings.INITIATOR_NAME;
    }

    public void offerAccepted(int flippedOffer) {
        this.inGame = false;
        String message = "I accept your offer.";

        if (turn.equals(Settings.INITIATOR_NAME)) {
            // Initiator accepted offer
            initiator.processOfferAccepted(newOffer);
            responder.processOfferAccepted(flippedOffer);
            setNewChips(newOffer, flippedOffer);
            this.initiator.addMessage(message);
        } else {
            // Responder accepted offer
            responder.processOfferAccepted(newOffer);
            initiator.processOfferAccepted(flippedOffer);
            setNewChips(flippedOffer, newOffer);
            this.responder.addMessage(message);
        }
        System.out.println("Offer accepted!!");
    }

    public void negotiationTerminates() {
        this.inGame = false;
        String message = "I end negotiation here";
        if (turn.equals(Settings.INITIATOR_NAME)) {
            this.initiator.addMessage(message);
        } else {
            this.responder.addMessage(message);
        }
        System.out.println("Negotiation terminated.");
    }

    public void setNewChips(int initChips, int respChips) {
        this.chipSets[getPlayerIdx(Settings.INITIATOR_NAME)] = initChips;
        this.chipSets[getPlayerIdx(Settings.RESPONDER_NAME)] = respChips;
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

    public void notifyListenersNewGame() {
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

    public int[] getBinMaxChips() {
        return this.binMaxChips;
    }

    /**
     * Calculates the number of chips in chip array
     *
     * @param chips the chip array
     * @return integer that represents the total number of chips.
     */
    public int calculateNumChips(int[] chips) {
        int sum = 0;
        for (int chip : chips)
            sum += chip;
        return sum;
    }

    public void setSimulationOn() {
        simulationOn = true;
    }
    public void setSimulationOff() {
        simulationOn = false;
    }
}