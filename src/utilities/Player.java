package utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Player: abstract class for players
 */
public abstract class Player {

    /**
     * The set of chips is represented as an index, the score resulting from the set of
     * chips is represented as an entry in the utilityFunction array
     */
    protected int chips;

    /**
     * Score for the player given a chip index
     */
    protected int[] utilityFunction;

    /**
     * The belief that an offer of a particular kind will be accepted.
     * The first entry is the sum of chips assigned extra to it and
     * the second entry is the sum of chips assigned less to it
     * This field is essentially to be able to learn across games.
     * TODO: How does pos and neg exactly work (first and second entry)?
     */
    private double[][] beliefsOfferType;

    /**
     * Initialized before each negotiation with the beliefsOfferType. In each negotiation,
     * the belief of each offer is adapted.
     * This field is essentially used to be able to learn from observations in current game.
     */
    private double[] beliefOffer;

    /**
     * To save the original beliefs when going to reason about possible offers.
     */
    private double[][] beliefOfferSaved;

    /**
     * Used to keep track of index of beliefOfferSaved.
     */
    protected int saveCount = 0;

    /**
     * Learning speed of the agent. Used to update beliefs about location and offers.
     * TODO: change this to a non-final double and add a Setter
     */
    private double learningSpeed = 0.1;

    /**
     * Name of the agent
     */
    private final String name;

    /**
     * Model of the game
     */
    protected final Game game;

    /**
     * List of messages that this agent sends
     */
    private List<String> messages;

    /**
     * Constructor
     *
     * @param namePlayer name of the agent
     * @param game       model of the game
     */
    public Player(String namePlayer, Game game) {
        this.name = namePlayer;
        this.game = game;
    }

    /**
     * Resets the player completely. That is, all beliefs are reset.
     *
     * @param chipsSelf       The chips to himself
     * @param chipsOther      The chips to the other player
     * @param utilityFunction The utility function for this player.
     */
    public void reset(int chipsSelf, int chipsOther, int[] utilityFunction) {
        this.messages = new ArrayList<>();
        this.chips = chipsSelf;
        this.utilityFunction = utilityFunction.clone();
        // TODO: saveCount initialized to 0?
        beliefOffer = new double[utilityFunction.length];
        beliefOfferSaved = new double[5][beliefOffer.length]; // TODO: Why 5?
        setupNewBeliefs();
    }

    /**
     * Set up new beliefs. The player starts with full belief that an offer of any type will work.
     */
    private void setupNewBeliefs() {
        int nrPossibleChips = Settings.CHIPS_PER_PLAYER * 2 + 1;
        beliefsOfferType = new double[nrPossibleChips][nrPossibleChips];
        for (int i = 0; i < nrPossibleChips; i++) {
            for (int j = 0; j < nrPossibleChips; j++) {
                beliefsOfferType[i][j] = 1.0;
            }
        }
        for (int i = 0; i < utilityFunction.length; i++) {
            beliefOffer[i] = getBeliefOfferType(i);
        }
    }

    /**
     * Prepares the player for a new round of negotiation. Only the beliefs of types of offers are kept.
     *
     * @param chipsSelf       The chips to himself
     * @param chipsOther      The chips to the other player
     * @param utilityFunction The utility function for this player
     */
    public void initNewRound(int chipsSelf, int chipsOther, int[] utilityFunction) {
        this.messages = new ArrayList<>();
        this.chips = chipsSelf;
        this.utilityFunction = utilityFunction.clone();
        beliefOffer = new double[utilityFunction.length];
        beliefOfferSaved = new double[5][beliefOffer.length];

        // Beliefs about specific colors are reset
        for (int i = 0; i < utilityFunction.length; i++) {
            beliefOffer[i] = getBeliefOfferType(i);
        }
    }

    /**
     * Gets the type of offer that will be made and return the belief that this offer with respect to this agent self
     * will be accepted by the other player. That is, if accepted, this player gets the offer.
     *
     * @param offerToSelf The offer this player wants to make to the other player with respect to this agent.
     *                    That is, if accepted, this player gets the offer.
     * @return The belief of the offer being accepted.
     */
    private double getBeliefOfferType(int offerToSelf) {
        int[] diff = Chips.getDifference(chips, offerToSelf, game.getBinMaxChips());
        int pos = Chips.getPositiveAmount(diff);
        int neg = Chips.getNegativeAmount(diff);
        return beliefsOfferType[pos][neg];
    }

    /**
     * Gives the agent's counter-offer for a specific offer
     *
     * @param offerReceived the offer made by Player p from the perspective of this agent.
     *                      That is, if accepted, the agent gets offer
     * @return the counter-offer to Player p from the perspective of Player p.
     * That is, if accepted, Player p gets the returned value.
     */
    abstract public int makeOffer(int offerReceived);

    /**
     * Selects the best offer to make.
     *
     * @param offerReceived the offer made by the other player from the perspective of this agent.
     * @return the counter-offer from the perspective of this player.
     * That is, if accepted, this player gets the returned value.
     */
    abstract public int selectOffer(int offerReceived);

    /**
     * Stores the current beliefs for later retrieval.
     * Used for prediction using a "fictitious play"-like structure
     */
    protected void saveBeliefs() {
        beliefOfferSaved[saveCount] = beliefOffer.clone();
        saveCount++;
    }

    /**
     * Restores previously stored beliefs
     */
    protected void restoreBeliefs() {
        saveCount--;
        beliefOffer = beliefOfferSaved[saveCount].clone();
    }

    /**
     * Returns the subjective probability that the partner would accept a given offer
     *
     * @param offerToSelf Offer to make to the partner
     * @return subjective probability that the offer is accepted.
     */
    protected double getBelief(int offerToSelf) {
        return beliefOffer[offerToSelf];
    }

    /**
     * Updates the beliefs of an offer when this offer was rejected.
     *
     * @param offerToSelf The offer with respect to the agent himself.
     */
    protected void updateBeliefsOfferRejected(int offerToSelf) {
        // TODO: when is this method called? -> offers are instantly rejected!!
        decreaseOfferTypeBelief(offerToSelf);
        decreaseColorBelief(offerToSelf);
    }

    /**
     * Decreases the belief of an offer of similar types to be accepted.
     * The belief is multiplied by (1 - learningSpeed).
     *
     * @param newOwnChips The offer to the agent self.
     */
    private void decreaseOfferTypeBelief(int newOwnChips) {
        int pos, neg;
        int[] diff = Chips.getDifference(chips, newOwnChips, game.getBinMaxChips());
        pos = Chips.getPositiveAmount(diff);
        neg = Chips.getNegativeAmount(diff);
        beliefsOfferType[pos][neg] *= (1 - learningSpeed);
    }

    /**
     * Decreases the belief of a specific offer being accepted.
     *
     * @param newOwnChips The new set of chips that has been offered.
     */
    private void decreaseColorBelief(int newOwnChips) {
        int i, j;
        int[] newOwnBins = Chips.getBins(newOwnChips, game.getBinMaxChips());
        int[] curOffer;
        for (i = 0; i < beliefOffer.length; i++) {
            // curOffer represents offer that agent wants himself
            curOffer = Chips.getBins(i, game.getBinMaxChips());
            for (j = 0; j < Settings.CHIP_DIVERSITY; j++) {
                if (curOffer[j] >= newOwnBins[j]) {
                    // curOffer demands at least as much chips of color j
                    // as the offer newOwnChips of the trading partner.
                    // It's likely to be rejected as well
                    beliefOffer[i] *= (1 - learningSpeed);
                }
            }
        }
    }

    /**
     * Revokes the previously assumed rejection of the offer. This is now undone by increasing the
     * offerTypeBelief again.
     *
     * @param offerMade The offer made by this player.
     */
    public void processOfferAccepted(int offerMade) {
        setNewChips(offerMade);
        increaseOfferTypeBelief(offerMade);
    }

    /**
     * Increases the belief of offers of a similar type to be more likely to be accepted too.
     *
     * @param newOwnChips The offer to this agent self.
     */
    private void increaseOfferTypeBelief(int newOwnChips) {
        int pos, neg;
        int[] diff = Chips.getDifference(chips, newOwnChips, game.getBinMaxChips());
        pos = Chips.getPositiveAmount(diff);
        neg = Chips.getNegativeAmount(diff);
        beliefsOfferType[pos][neg] = beliefsOfferType[pos][neg] * (1 - learningSpeed) + learningSpeed;
    }

    /**
     * This method is called when an offer is received. It updates the beliefs of offers being accepted.
     *
     * @param offerReceived the offer received
     */
    protected void receiveOffer(int offerReceived) {
        updateBeliefsOfferReceived(offerReceived);
    }

    /**
     * This method is called when this agent sends an offer. The beliefs are updated for when this offer
     * is rejected. If this offer is not rejected, the values are revoked.
     *
     * @param offerToSelf The offer this agent makes with respect to himself.
     */
    protected void sendOffer(int offerToSelf) {
        updateBeliefsOfferRejected(offerToSelf);
    }

    /**
     * Updates beliefs based on receiving an offer.
     *
     * @param offerReceived The offer received.
     */
    protected void updateBeliefsOfferReceived(int offerReceived) {
        increaseOfferTypeBelief(offerReceived);
        increaseColorBelief(offerReceived);
    }

    /**
     * Decreases the belief of an offer being accepted that assigns more of a particular color to this agent.
     * This is called when an offer has been made which assigns newOwnChips to this agent.
     * TODO: does belief also increase in some cases here? E.g., when more chips of a color is given.
     *
     * @param newOwnChips The chips assigned to this agent
     */
    private void increaseColorBelief(int newOwnChips) {
        int i, j;
        int[] newOwnBins = Chips.getBins(newOwnChips, game.getBinMaxChips());
        int[] curOffer;
        for (i = 0; i < beliefOffer.length; i++) {
            // curOffer represents offer to agent wants himself
            curOffer = Chips.getBins(i, game.getBinMaxChips());
            for (j = 0; j < Settings.CHIP_DIVERSITY; j++) {
                if (curOffer[j] > newOwnBins[j]) {
                    // curOffer demands more chips of color j than the
                    // offer newOwnChips of the trading partner.
                    // It's less likely to be accepted.
                    beliefOffer[i] *= (1 - learningSpeed);
                }
            }
        }
    }


    /**
     * The belief of the offer being accepted times the utility plus the expected value of the offer being rejected - 1
     * for the offer being made.
     *
     * @param offer offer the player wants to keep
     * @return the expected value of this offer
     */
    protected double getExpectedValue(int offer) {
        // TODO: ASK ABOUT THIS!! -> ?Different method than in article? See other class for another different method.
        double belief = getBelief(offer);
        return belief * utilityFunction[offer] + (1 - belief) * utilityFunction[chips] - 1;
    }


    /**
     * Adds a message to the messages this agent sent
     *
     * @param message The message to add.
     */
    public void addMessage(String message) {
        messages.add(message);
    }

    /**
     * returns the utility score of this agent given its current chips
     *
     * @return the utility score of this agent
     */
    public int getUtilityValue() {
        return utilityFunction[chips];
    }


    ///////////////////////////////
    //--- SETTERS AND GETTERS ---//
    ///////////////////////////////

    /**
     * Setter for new chips
     *
     * @param newChips The new chips this agent will possess
     */
    public void setNewChips(int newChips) {
        this.chips = newChips;
    }

    /**
     * Setter for learning speed
     *
     * @param newLearningSpeed The new learning speed of the agent
     */
    public void setLearningSpeed(double newLearningSpeed) {
        this.learningSpeed = newLearningSpeed;
    }

    /**
     * Getter for learning speed
     *
     * @return Gets the learning speed
     */
    public double getLearningSpeed() {
        return this.learningSpeed;
    }

    /**
     * Getter for the chips in the form of bins.
     *
     * @return The chips of this player in the form of bins.
     */
    public int[] getChipsBin() {
        // TODO: Is it faster to also save chipsBin in player?
        return Chips.getBins(chips, game.getBinMaxChips());
    }

    /**
     * Getter for name
     *
     * @return the name of the agent
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for game
     *
     * @return the game model of the agent
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Getter for messages
     *
     * @return list of messages
     */
    public List<String> getMessages() {
        return this.messages;
    }
}
