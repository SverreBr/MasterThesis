package lyingAgents.model;

import lyingAgents.utilities.*;
import lyingAgents.model.player.PlayerLying;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * lyingAgents.model.Game class: the two players and the coloured trails board
 */
public class Game {

    public static final boolean DEBUG = false;

    /**
     * bin with all the chips in the game
     */
    private int[] binMaxChips;

    /**
     * utility functions (utility as a function of offer) for each possible goal location
     */
    private int[][] utilityFunctions;

    /**
     * Array that contains the complementary offer
     */
    private int[] flipArray;

    /**
     * A 2-dimensional array which contains two goal positions for the initiator and the responder
     */
    private final int[] goalPositions = new int[2];

    /**
     * A mapping from goal position as an integer to the goal position as a point
     */
    private final Map<Integer, Point> goalPositionsDict;

    /**
     * the initiator agent
     */
    private PlayerLying initiator;

    /**
     * the last offer made
     */
    private int lastOfferMade;

    /**
     * Total number of offers made in negotiation
     */
    private int totalNrOffersMade;

    /**
     * the responding agent
     */
    private PlayerLying responder;

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
    private boolean isGameFinished;

    /**
     * A field to check if the simulation (visuals) are on or off
     */
    private boolean simulationOn = true;

    /**
     * Field to keep track of whether an agent sent a message to the trading partner
     */
    private boolean isMessageSend = false;

    /**
     * The message sent by one of the agents
     */
    private String messageSend;

    private boolean reachedMaxNumOffers;

    /**
     * Constructor
     */
    public Game(int initToM, int respToM, double initLR, double respLR, boolean initCanLie, boolean respCanLie, boolean initCanSendMessages, boolean respCanSendMessages) {
        this.listeners = new HashSet<>();
        this.board = new Board();
        this.goalPositionsDict = MiscFunc.makeGoalPositionDictionary();
        initNewGame(initToM, respToM, initLR, respLR, initCanLie, respCanLie, initCanSendMessages, respCanSendMessages);
    }

    /**
     * resets the board to a new initialization
     */
    public void reset(int initToM, int respToM, double initLR, double respLR, boolean initCanLie, boolean respCanLie, boolean initCanSendMessages, boolean respCanSendMessages) {
        this.board.resetBoard();
        this.initNewGame(initToM, respToM, initLR, respLR, initCanLie, respCanLie, initCanSendMessages, respCanSendMessages);
        if (simulationOn) notifyListenersNewGame();
    }

    /**
     * Initializes a fully new game, where the agents are also fully reset
     */
    private void initNewGame(int initToM, int respToM, double initLR, double respLR, boolean initCanLie, boolean respCanLie, boolean initCanSendMessages, boolean respCanSendMessages) {
        GameSetting gameSetting = generateNewNegotiationSetting();
        int chipSetInitiator = Chips.getIndex(gameSetting.getChipSets()[0], binMaxChips);
        int chipSetResponder = Chips.getIndex(gameSetting.getChipSets()[1], binMaxChips);


        this.initiator = new PlayerLying(Settings.INITIATOR_NAME, this, initToM, initLR,
                chipSetInitiator, chipSetResponder, utilityFunctions[goalPositions[0]], initCanLie, initCanSendMessages);
        this.responder = new PlayerLying(Settings.RESPONDER_NAME, this, respToM, respLR,
                chipSetResponder, chipSetInitiator, utilityFunctions[goalPositions[1]], respCanLie, respCanSendMessages);
    }

    /**
     * Initializes a new round of play, where agents keep learnt behaviour across games.
     */
    public void newRound() {
        this.board.resetBoard();
        GameSetting gameSetting = generateNewNegotiationSetting();
        int chipSetInitiator = Chips.getIndex(gameSetting.getChipSets()[0], binMaxChips);
        int chipSetResponder = Chips.getIndex(gameSetting.getChipSets()[1], binMaxChips);

        this.initiator.initNegotiationRound(chipSetInitiator, chipSetResponder, utilityFunctions[goalPositions[0]]);
        this.responder.initNegotiationRound(chipSetResponder, chipSetInitiator, utilityFunctions[goalPositions[1]]);
        if (simulationOn) notifyListenersNewGame();
    }

    /**
     * Generates a new negotiation setting.
     */
    private GameSetting generateNewNegotiationSetting() {
        setBasicNewGameSettings();

        generateGoalPositions();
        int[][] newChips = generateNewChips();
        GameSetting gameSetting = new GameSetting(board.getBoard(), newChips, goalPositions);
        calculateSetting(gameSetting);

        return gameSetting;
    }

    /**
     * Calculates various things for a new negotiation settings. Utility functions for goal positions are
     * calculated.
     */
    private void calculateSetting(GameSetting gameSetting) {
        if (DEBUG) System.out.println("\n-------------------- NEW ROUND --------------------");
        int numIndexCodes, pos;
        int[][] chipsSets = gameSetting.getChipSets();
        this.binMaxChips = Chips.makeNewChipBin();

        numIndexCodes = 1;
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            this.binMaxChips[i] = chipsSets[0][i] + chipsSets[1][i];
            numIndexCodes *= (this.binMaxChips[i] + 1);
        }

        Point goalPosition;
        utilityFunctions = new int[this.goalPositionsDict.size()][numIndexCodes];
        for (Map.Entry<Integer, Point> entry : goalPositionsDict.entrySet()) {
            pos = entry.getKey();
            goalPosition = entry.getValue();
            calcUtilityFunction(utilityFunctions[pos], goalPosition);
        }
        makeNewFlipArrayOffer(numIndexCodes);
    }

    /**
     * Calculates the utility function for a given goal position
     *
     * @param utilityFunction The utility function to write to
     * @param goalPosition    The goal position
     */
    private void calcUtilityFunction(int[] utilityFunction, Point goalPosition) {
        int[] offer;
        for (int offerIdx = 0; offerIdx < utilityFunction.length; offerIdx++) {
            offer = Chips.getBins(offerIdx, binMaxChips);
            utilityFunction[offerIdx] = board.calculateScore(Settings.STARTING_POSITION, offer, Settings.STARTING_POSITION, goalPosition);
        }
    }

    /**
     * Makes a new array to get the flipped offer of a given offer
     *
     * @param numIndexCodes The offer that is needed to be flipped
     */
    private void makeNewFlipArrayOffer(int numIndexCodes) {
        flipArray = new int[numIndexCodes];
        for (int offer = 0; offer < numIndexCodes; offer++) {
            flipArray[offer] = Chips.invert(offer, binMaxChips);
        }
    }

    /**
     * Generate and distribute tokens for the agents
     */
    private int[][] generateNewChips() {
        int[][] newChips = new int[2][];
        int newChip;

        for (int agent = 0; agent < 2; agent++) {
            newChips[agent] = Chips.makeNewChipBin();
            for (int numChip = 0; numChip < Settings.CHIPS_PER_PLAYER; numChip++) {
                newChip = (int) (Math.random() * Settings.CHIP_DIVERSITY);
                newChips[agent][newChip] += 1;
            }
        }

        return newChips;
    }

    /**
     * Sets a new game setting to the game
     *
     * @param gameSetting The game setting to be set to this game
     */
    public void newGameSettings(GameSetting gameSetting) {
        setBasicNewGameSettings();
        board.makeBoard(gameSetting.getBoard());
        goalPositions[0] = gameSetting.getGoalPositions()[0];
        goalPositions[1] = gameSetting.getGoalPositions()[1];

        calculateSetting(gameSetting);

        int chipsInitiator = Chips.getIndex(gameSetting.getChipSets()[0], binMaxChips);
        int chipsResponder = Chips.getIndex(gameSetting.getChipSets()[1], binMaxChips);

        this.initiator.initNegotiationRound(chipsInitiator, chipsResponder, utilityFunctions[goalPositions[0]]);
        this.responder.initNegotiationRound(chipsResponder, chipsInitiator, utilityFunctions[goalPositions[1]]);
        if (simulationOn) notifyListenersNewGame();
    }

    /**
     * Sets some basic values to the game model
     */
    private void setBasicNewGameSettings() {
        setBooleanGameFinished(false);
        this.lastOfferMade = Settings.ID_NO_OFFER;
        this.totalNrOffersMade = 0;
        this.turn = Settings.INITIATOR_NAME;
        this.reachedMaxNumOffers = false;
    }

    //////////////////////////////
    // --- Playing the game --- //
    //////////////////////////////

    /**
     * A step in the game. The initiator and responder take turns making offer. It is then checked whether
     * the agent ends negotiation, accepted an offer or made a new offer.
     */
    public void step() {
        int tmpNewOffer;

        if (isGameFinished) return;

        if (turn.equals(Settings.INITIATOR_NAME)) {
            if (isMessageSend) {
                this.initiator.receiveMessage(messageSend);
                isMessageSend = false;
            }
            tmpNewOffer = this.initiator.makeOffer(lastOfferMade);
        } else {
            if (isMessageSend) {
                this.responder.receiveMessage(messageSend);
                isMessageSend = false;
            }
            tmpNewOffer = this.responder.makeOffer(lastOfferMade);
        }

        if (tmpNewOffer == Settings.ID_WITHDRAW_NEGOTIATION) { // Negotiation terminated
            negotiationTerminates();
            if (DEBUG) printFinalStatements();
        } else if (tmpNewOffer == Settings.ID_ACCEPT_OFFER) { // Offer is accepted
            offerAccepted();
            if (DEBUG) printFinalStatements();
        } else { // Negotiation continues with new offer
            totalNrOffersMade++;
            lastOfferMade = flipOffer(tmpNewOffer);
            addOfferMessage(tmpNewOffer);
            switchTurn();
        }

        if (simulationOn) notifyListeners();
    }

    private void printFinalStatements() {
        List<OfferOutcome> peList = getStrictParetoOutcomes();

        System.out.println("\nPossible better outcomes than initial situation:");
        for (OfferOutcome offerOutcome : peList) {
            System.out.println("\t " + Arrays.toString(Chips.getBins(offerOutcome.getOfferForInit(), getBinMaxChips())) +
                    " - " + Arrays.toString(Chips.getBins(flipOffer(offerOutcome.getOfferForInit()), getBinMaxChips())) +
                    "; " + offerOutcome.getValueInit() + " - " + offerOutcome.getValueResp() +
                    "; sw=" + offerOutcome.getSocialWelfare());
        }

        if (peList.size() == 0) {
            System.out.println("\t- THERE WAS NO STRICT PARETO IMPROVEMENT FROM THE INITIAL SETTING");
            System.out.println("\t- THERE WAS NO STRICT SOCIAL WELFARE IMPROVEMENT FROM THE INITIAL SETTING");
        } else {
            boolean isPE = true;
            int sw = responder.getUtilityValue() + initiator.getUtilityValue();
            System.out.println("\t- Current sw = " + sw);
            int respUtil = responder.getUtilityValue();
            int initUtil = initiator.getUtilityValue();
            int highestSW = peList.get(0).getSocialWelfare();

            for (OfferOutcome outcome : peList) {
                highestSW = Math.max(highestSW, outcome.getSocialWelfare());
                if ((((outcome.getValueInit() > initUtil) && (outcome.getValueResp() >= respUtil))) ||
                        (((outcome.getValueInit() >= initUtil) && (outcome.getValueResp() > respUtil)))) {
                    isPE = false;
                }
            }
            System.out.println("\t- THERE WAS NO PARETO IMPROVEMENT ANYMORE: " + isPE);
            System.out.println("\t- THERE WAS NO HIGHER SW POSSIBLE: " + (highestSW == sw));
        }
    }

    /**
     * Plays the negotiation until MAX_NUMBER_OFFERS of steps have been performed or the game ended
     */
    public void playTillEnd() {
        int i = 0;
        while (i < Settings.MAX_NUMBER_OFFERS && !isGameFinished) {
            step();
            i++;
        }
        if (i >= Settings.MAX_NUMBER_OFFERS) {
            System.out.println("--- " + Settings.MAX_NUMBER_OFFERS + " STEPS PERFORMED. ---");
            reachedMaxNumOffers = true;
        }
    }

    /**
     * Method called when an agent sends a message to another agent
     *
     * @param message The message that is sent
     */
    public void sendMessage(String message) {
        isMessageSend = true;
        this.messageSend = message;
    }

    //////////////////////////////
    // --- Helper functions --- //
    //////////////////////////////

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

    /**
     * assigns a goal position to each agent
     */
    private void generateGoalPositions() {
        for (int i = 0; i < goalPositions.length; i++) {
            goalPositions[i] = (int) (Math.random() * goalPositionsDict.size());
        }
    }

    /**
     * Adds an offer message to an offer
     */
    public void addOfferMessage(int flippedOffer) {
        String message;
        if (turn.equals(Settings.INITIATOR_NAME)) {
            message = "I offer: " + Arrays.toString(Chips.getBins(flippedOffer, binMaxChips)) + " - " +
                    Arrays.toString(Chips.getBins(lastOfferMade, binMaxChips));
            this.initiator.addMessage(message, true);
        } else {
            message = "I offer: " + Arrays.toString(Chips.getBins(lastOfferMade, binMaxChips)) + " - " +
                    Arrays.toString(Chips.getBins(flippedOffer, binMaxChips));
            this.responder.addMessage(message, true);
        }
    }

    /**
     * Switches turn
     */
    public void switchTurn() {
        turn = turn.equals(Settings.INITIATOR_NAME) ? Settings.RESPONDER_NAME : Settings.INITIATOR_NAME;
    }

    /**
     * Called when an offer is accepted
     */
    public void offerAccepted() {
        setBooleanGameFinished(true);

        if (turn.equals(Settings.INITIATOR_NAME)) {
            // Initiator accepted offer
            initiator.processOfferAccepted(lastOfferMade, false);
            responder.processOfferAccepted(flipOffer(lastOfferMade), true);
            this.initiator.addMessage(Settings.ACCEPT_OFFER_MESSAGE, false);
        } else {
            // Responder accepted offer
            initiator.processOfferAccepted(flipOffer(lastOfferMade), true);
            responder.processOfferAccepted(lastOfferMade, false);
            this.responder.addMessage(Settings.ACCEPT_OFFER_MESSAGE, false);
        }
    }

    /**
     * Method called when negotiation is terminated.
     */
    public void negotiationTerminates() {
        setBooleanGameFinished(true);
        if (turn.equals(Settings.INITIATOR_NAME)) {
            this.initiator.addMessage(Settings.TERMINATE_NEGOTIATION_MESSAGE, false);
        } else {
            this.responder.addMessage(Settings.TERMINATE_NEGOTIATION_MESSAGE, false);
        }
    }

    /**
     * when a change occurs to the game, the listeners are notified with this method
     */
    protected void notifyListeners() {
        for (GameListener gameListener : this.listeners) {
            gameListener.gameChanged();
        }
    }

    /**
     * Method called to notify all listeners that a new game has started
     */
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

    public List<OfferOutcome> getStrictParetoOutcomes() {
        OfferOutcome newOffer;
        List<OfferOutcome> strictParetoOutcomes = new ArrayList<>();
        int[] utilityFuncInit = getUtilityFunction(getGoalPositionPlayer(Settings.INITIATOR_NAME));
        int[] utilityFuncResp = getUtilityFunction(getGoalPositionPlayer(Settings.RESPONDER_NAME));
        OfferOutcome initOutcome = new OfferOutcome(initiator.getInitialChips(), utilityFuncInit[initiator.getInitialChips()], utilityFuncResp[responder.getInitialChips()]);
        for (int offer = 0; offer < utilityFuncInit.length; offer++) {
            newOffer = new OfferOutcome(offer, utilityFuncInit[offer], utilityFuncResp[flipOffer(offer)]);
            if (((newOffer.getValueInit() > initOutcome.getValueInit()) && (newOffer.getValueResp() > initOutcome.getValueResp()))) {
                strictParetoOutcomes.add(newOffer);
            }
        }

        return strictParetoOutcomes;
    }

    public List<OfferOutcome> getParetoOutcomes() {
        OfferOutcome newOffer;
        List<OfferOutcome> paretoOutcomes = new ArrayList<>();
        int[] utilityFuncInit = getUtilityFunction(getGoalPositionPlayer(Settings.INITIATOR_NAME));
        int[] utilityFuncResp = getUtilityFunction(getGoalPositionPlayer(Settings.RESPONDER_NAME));
        OfferOutcome initOutcome = new OfferOutcome(initiator.getInitialChips(), utilityFuncInit[initiator.getInitialChips()], utilityFuncResp[responder.getInitialChips()]);
        for (int offer = 0; offer < utilityFuncInit.length; offer++) {
            newOffer = new OfferOutcome(offer, utilityFuncInit[offer], utilityFuncResp[flipOffer(offer)]);
            if (((newOffer.getValueInit() >= initOutcome.getValueInit()) && (newOffer.getValueResp() > initOutcome.getValueResp())) ||
                    ((newOffer.getValueInit() > initOutcome.getValueInit()) && (newOffer.getValueResp() >= initOutcome.getValueResp()))) {
                paretoOutcomes.add(newOffer);
            }
        }

        return paretoOutcomes;
    }

    ///////////////////////////////
    //--- Setters and getters ---//
    ///////////////////////////////

    /**
     * Gets the number of offers made in this game.
     *
     * @return The number of offers made in this game.
     */
    public int getTotalNrOffersMade() {
        return this.totalNrOffersMade;
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
     * Getter for whether the game is still active
     *
     * @return true if in game; false otherwise
     */
    public boolean isGameFinished() {
        return this.isGameFinished;
    }

    /**
     * Sets the field isGameFinished
     *
     * @param gameFinished True or false
     */
    public void setBooleanGameFinished(boolean gameFinished) {
        this.isGameFinished = gameFinished;
        if (simulationOn) {
            for (GameListener gameListener : this.listeners) {
                gameListener.inGameChanged();
            }
        }
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
    public PlayerLying getInitiator() {
        return this.initiator;
    }

    /**
     * Getter for responder agent
     *
     * @return the agent that is the responder
     */
    public PlayerLying getResponder() {
        return this.responder;
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

    /**
     * Gets the number of possible goal positions
     *
     * @return The number of goal positions
     */
    public int getNumberOfGoalPositions() {
        return this.goalPositionsDict.size();
    }

    /**
     * Gets the player index corresponding to player name p
     */
    public int getPlayerIdx(String p) {
        return p.equals(Settings.INITIATOR_NAME) ? 0 : 1;
    }

    /**
     * Gets the total chips in the game as a bin
     *
     * @return The bin that contains all chips in the game
     */
    public int[] getBinMaxChips() {
        return this.binMaxChips;
    }

    /**
     * Gets the goal position of the player
     *
     * @param player The players name
     * @return The goal position as a point
     */
    public Point getGoalPositionPointPlayer(String player) {
        return goalPositionsDict.get(goalPositions[getPlayerIdx(player)]);
    }

    /**
     * Gets the goal position of the player as an integer
     *
     * @param player The players name
     * @return The goal positions as an integer
     */
    public int getGoalPositionPlayer(String player) {
        return goalPositions[getPlayerIdx(player)];
    }

    /**
     * Gets goal positions of the initiator and the responder
     *
     * @return An array with goal positions of the initiator and the responder
     */
    public int[] getGoalPositions() {
        return goalPositions;
    }

    /**
     * Sets the simulation (visuals) on so that listeners get notified
     */
    public void setSimulationOn() {
        simulationOn = true;
    }

    /**
     * Sets the simulation (visuals) off so that listeners do not get notified
     */
    public void setSimulationOff() {
        simulationOn = false;
    }

    /**
     * Checks if the simulation (visuals) is on or off
     *
     * @return True if simulation is on, false otherwise
     */
    public boolean isSimulationOff() {
        return !simulationOn;
    }

    /**
     * Gets the goal position dictionary
     *
     * @return Map with integer to goal position point
     */
    public Map<Integer, Point> getGoalPositionsDict() {
        return goalPositionsDict;
    }

    /**
     * Gets the game setting of the game model
     *
     * @return Game setting of this game model
     */
    public GameSetting getGameSetting() {
        return new GameSetting(this);
    }

    public boolean isReachedMaxNumOffers() {
        return reachedMaxNumOffers;
    }
}