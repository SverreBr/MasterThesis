package lyingAgents.model.player;

import lyingAgents.model.Game;
import lyingAgents.utilities.Chips;
import lyingAgents.utilities.Messages;
import lyingAgents.utilities.RandomNumGen;
import lyingAgents.utilities.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PlayerLying class: An agent that uses theory of mind and might lie to the other player. Players of this class can
 * send messages to another player. An agent lies by communicating a wrong location.
 */
public class PlayerLying extends PlayerToM {

    /**
     * True if an agent can lie, that is, it can send messages that are not true.
     */
    private final boolean canMakeFalseStatements;

    private final boolean canSendMessages;

    private double PROB_TOM0_SEND_MESSAGE = 0.25;

    /**
     * List of best offers
     */
    private List<OfferType> bestOffers;

    /**
     * Value for calculating the best offer to make.
     */
    private double tmpSelectOfferValue;

    private RandomNumGen rng;

    /**
     * @param playerName      name of the player
     * @param game            game model
     * @param orderToM        order of theory of mind
     * @param learningSpeed   learning speed
     * @param chipsSelf       offer that are for this player
     * @param chipsOther      offer that are for the other player
     * @param utilityFunction utility function for this player
     */
    public PlayerLying(String playerName, Game game, int orderToM, double learningSpeed, int chipsSelf, int chipsOther,
                       int[] utilityFunction, boolean agentCanLie, boolean canSendMessages) {
        super(playerName, game, orderToM, learningSpeed, chipsSelf, chipsOther, utilityFunction);
        if (agentCanLie & !canSendMessages) {
            System.err.println("Agent cannot lie if it cannot send messages. Agent can lie set to false.");
            agentCanLie = false;
        }
        this.canMakeFalseStatements = agentCanLie;
        this.canSendMessages = canSendMessages;
        initNegotiationRound(chipsSelf, chipsOther, utilityFunction);
    }

    @Override
    public void initNegotiationRound(int chipsSelf, int chipsOther, int[] utilityFunction) {
        super.initNegotiationRound(chipsSelf, chipsOther, utilityFunction);
        if (canSendMessages && (getOrderToM() == 0)) makeRng();
    }

    private void makeRng() {
        double[] zeroOrderProbSendingMessages = new double[game.getNumberOfGoalPositions()];
        int[] goalPositionsArray = new int[game.getNumberOfGoalPositions()];
        int goalPosition = this.game.getGoalPositionPlayer(this.getName());

        Arrays.fill(zeroOrderProbSendingMessages, Settings.PROB_MASS_OTHER_LOCS);
        zeroOrderProbSendingMessages[goalPosition] = 1.0 - Settings.PROB_MASS_OTHER_LOCS * (zeroOrderProbSendingMessages.length - 1);


        for (int i = 0; i < goalPositionsArray.length; i++) {
            goalPositionsArray[i] = i;
        }
        rng = new RandomNumGen(goalPositionsArray, zeroOrderProbSendingMessages);
    }

    /**
     * Method to receive offerReceived and make curOffer.
     *
     * @param offerReceived the offer made by Player p from the perspective of this agent.
     *                      That is, if accepted, the agent gets offer
     * @return The best offer to make, that is, when accepted, this player gets the curOffer.
     */
    public int makeOffer(int offerReceived) {
        int curOffer;

        if (Game.DEBUG) System.out.println("\n-----");

        if (offerReceived == Settings.ID_NO_OFFER) {
            curOffer = chooseOffer(Settings.ID_NO_OFFER);
        } else {
            receiveOffer(offerReceived);
            curOffer = chooseOffer(offerReceived);
        }
        if ((curOffer != Settings.ID_ACCEPT_OFFER) && (curOffer != Settings.ID_WITHDRAW_NEGOTIATION)) sendOffer(curOffer);
        return curOffer;
    }

    public int chooseOffer(int offerReceived) {
        List<OfferType> offerList = selectBestOffers(offerReceived);
        OfferType offerType = offerList.get((int) (Math.random() * offerList.size()));
        int newOffer = offerType.getOffer();

        if (canSendMessages) {
            int locMessage = offerType.getLoc();
            if ((getOrderToM() == 0) && (newOffer != Settings.ID_ACCEPT_OFFER) && (newOffer != Settings.ID_WITHDRAW_NEGOTIATION)
                    && (Math.random() < PROB_TOM0_SEND_MESSAGE)) locMessage = rng.random();
            if (locMessage != Settings.ID_NO_LOCATION) sendMessage(Messages.createLocationMessage(locMessage));
        }


        if (Game.DEBUG) {
            System.out.println("\n-> Chosen is offer: " + newOffer + " with value " + Settings.PRINT_DF.format(offerType.getValue()) + "\n");
            printExpectedResponse(offerType);
        }

        return newOffer;
    }

    /**
     * Selects the best offer to make as a response to offerReceived. An agent can send an additional message containing
     * a location. All combinations are explored.
     *
     * @param offerReceived the offer made by the other player from the perspective of this agent.
     * @return The best offer as a response to offerReceived.
     */
    @Override
    public List<OfferType> selectBestOffers(int offerReceived) {
        double noMessageOfferValue;

        if (!Game.DEBUG && !canSendMessages) return super.selectBestOffers(offerReceived);

        bestOffers = new ArrayList<>();
        tmpSelectOfferValue = -Double.MAX_VALUE + Settings.EPSILON;
        addOffersWithoutMessage();  // send no message

        // Choose offer without message if at least as good as with a message.
        List<OfferType> bestOffersWithoutMessage = new ArrayList<>(bestOffers);
        noMessageOfferValue = tmpSelectOfferValue;
        tmpSelectOfferValue = -Double.MAX_VALUE + Settings.EPSILON;
        bestOffers = new ArrayList<>();

        if (canSendMessages && (getOrderToM() > 0)) {
            for (int loc = 0; loc < this.game.getNumberOfGoalPositions(); loc++) {
                addOffers(loc);
            }
        }

        if (Game.DEBUG) {
            System.out.println("-> Offers that are optimal for " + getName() + " including messages:");
            for (OfferType smt : bestOffers) {
                System.out.println("\t- value=" + Settings.PRINT_DF.format(smt.getValue()) + "; offer to self=" + smt.getOffer() + "; " +
                        Arrays.toString(Chips.getBins(smt.getOffer(), game.getBinMaxChips())) +
                        ", loc=" + smt.getLoc());
            }

            System.out.println("-> Offers that are optimal for " + getName() + " without messages:");
            for (OfferType smt : bestOffersWithoutMessage) {
                System.out.println("\t- value=" + Settings.PRINT_DF.format(smt.getValue()) + "; offer to self=" + smt.getOffer() + "; " +
                        Arrays.toString(Chips.getBins(smt.getOffer(), game.getBinMaxChips())) +
                        ", loc=" + smt.getLoc());
            }
        }

        if (noMessageOfferValue + Settings.EPSILON >= tmpSelectOfferValue) {
            bestOffers = bestOffersWithoutMessage;
            tmpSelectOfferValue = noMessageOfferValue;
        } else if (Game.DEBUG) {
            boolean lyingIsBetter = true;
            System.out.println("--- THERE IS AN OFFER WITH MESSAGE THAT IS BETTER THAN SENDING NO MESSAGE ---");

            // Check if lying is better than telling the truth.
            for (OfferType something : bestOffers) {
                if (something.getLoc() == game.getGoalPositionPlayer(this.getName())) {
                    lyingIsBetter = false;
                }
            }
            if (lyingIsBetter) System.out.println("--- LYING IS BETTER THAN TELLING THE TRUTH. ---");
        }

        return checkPossibleOffers(offerReceived, bestOffers, tmpSelectOfferValue);
    }


    /**
     * Method to add offers with a certain location message
     *
     * @param loc The location to message to the other agent
     */
    private void addOffers(int loc) {
        double curValue;
        boolean savedHasSentMessage = this.hasSentMessage;

        if ((!canMakeFalseStatements) && (loc != game.getGoalPositionPlayer(this.getName()))) return;
        assert (getOrderToM() > 0) : "Theory of mind zero agent cannot reason about sending messages...";

        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            partnerModel.saveBeliefs();
            partnerModel.receiveMessage(Messages.createLocationMessage(loc));
            curValue = getValue(i);
            partnerModel.restoreBeliefs();
            this.setHasSentMessage(savedHasSentMessage);
            processOffer(i, curValue, loc);
        }

    }

    /**
     * Adds offers without a message.
     */
    private void addOffersWithoutMessage() {
        double curValue;
        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            curValue = getValue(i);
            processOffer(i, curValue, Settings.ID_NO_LOCATION);
        }
    }

    /**
     * Processes an offer and a location message with the curValue as utility. Adds offer with message to best offers
     * when it is exactly as good as the other offers, and makes a new list when it is better than all previously
     * explored offers.
     *
     * @param offer    The offer made
     * @param location The location message
     * @param curValue The value of the combination of offer and location
     */
    protected void processOffer(int offer, double curValue, int location) {
        if (curValue - Settings.EPSILON > tmpSelectOfferValue) {
            tmpSelectOfferValue = curValue;
            bestOffers = new ArrayList<>();
            bestOffers.add(new OfferType(offer, curValue, location));
        } else if ((curValue >= tmpSelectOfferValue - Settings.EPSILON) &&
                (curValue <= tmpSelectOfferValue + Settings.EPSILON)) {
            bestOffers.add(new OfferType(offer, curValue, location));
        }
    }


    /**
     * Method called when a message has been sent.
     *
     * @param message The message to be sent
     */
    public void sendMessage(String message) {
        super.sendMessage(message);
        addMessage(message, false);
        this.game.sendMessage(message);
    }

    /**
     * Getter for whether this agent can lie.
     *
     * @return True if this agent can lie, false otherwise.
     */
    public boolean isCanMakeFalseStatements() {
        return canMakeFalseStatements;
    }

    public boolean isCanSendMessages() {
        return canSendMessages;
    }

    public void setPROB_TOM0_SEND_MESSAGE(double prob) {
        PROB_TOM0_SEND_MESSAGE = prob;
    }

    public double getPROB_TOM0_SEND_MESSAGE() {
        return PROB_TOM0_SEND_MESSAGE;
    }
}