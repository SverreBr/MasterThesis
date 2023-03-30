package lyingAgents.model.player;

import lyingAgents.model.Game;
import lyingAgents.utilities.Chips;
import lyingAgents.utilities.Messages;
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
    private final boolean canLie;

    /**
     * List of best offers
     */
    private List<LyingOfferType> bestOffers;

    /**
     * Value for calculating the best offer to make.
     */
    private double tmpSelectOfferValue;

    /**
     * True if there is best offer without sending a message.
     */
    private boolean thereIsBestOfferWithoutMessage;

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
                       int[] utilityFunction, boolean agentCanLie) {
        super(playerName, game, orderToM, learningSpeed, chipsSelf, chipsOther, utilityFunction);
        if (agentCanLie && (orderToM < 2)) {
            System.out.println("Agent cannot lie if not theory of mind greater than 1.");
            agentCanLie = false;
        }
        this.canLie = agentCanLie;
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

        if (offerReceived < 0) {
            curOffer = selectInitialOffer();
        } else {
            System.out.println("\n" + getName() + ": Value offer received= " + utilityFunction[offerReceived]);
            receiveOffer(offerReceived);
            curOffer = selectOffer(offerReceived);
        }
        sendOffer(curOffer);
        return curOffer;
    }

    /**
     * Method to select the initial offer
     *
     * @return The best offer to make, that is, when accepted, this player gets the offer.
     */
    public int selectInitialOffer() {
        return selectOffer(chips);
    }

    /**
     * Selects the best offer to make as a response to offerReceived. An agent can send an additional message containing
     * a location. All combinations are explored.
     *
     * @param offerReceived the offer made by the other player from the perspective of this agent.
     * @return The best offer as a response to offerReceived.
     */
    public int selectOffer(int offerReceived) {
        bestOffers = new ArrayList<>();
        LyingOfferType bestLyingOfferType;
        int bestOffer, bestLoc, goalPosition;
        double noMessageOfferValue;

        bestOffers.add(new LyingOfferType(this.chips, -1));  // withdraw from negotiation as basis
        tmpSelectOfferValue = utilityFunction[this.chips];
        addOffersWithoutMessage(offerReceived);  // send no message

        // Choose offer without message if at least as good as with a message.
        List<LyingOfferType> bestOffersWithoutMessage = new ArrayList<>(bestOffers);
        bestLyingOfferType = bestOffers.get((int) (Math.random() * bestOffers.size()));
        noMessageOfferValue = tmpSelectOfferValue;
        thereIsBestOfferWithoutMessage = true;

        if (getOrderToM() > 1) {  // agent models that trading partner beliefs this agent has a goal position
            if (canLie) {
                for (int loc = 0; loc < this.game.getNumberOfGoalPositions(); loc++) {
                    addOffers(loc, offerReceived);
                }
            } else {
                goalPosition = game.getGoalPositionPlayer(this.getName());
                addOffers(goalPosition, offerReceived);
            }
        }

        System.out.println("-> Offers that are optimal without message:");
        for (LyingOfferType smt : bestOffersWithoutMessage) {
            System.out.println("\t- value=" + Settings.PRINT_DF.format(noMessageOfferValue) + "; offer to self=" + smt.offer() + "; " +
                    Arrays.toString(Chips.getBins(smt.offer(), game.getBinMaxChips())));
        }
        System.out.println("\t- Chosen is offer to self: " + bestLyingOfferType.offer() + "; " +
                Arrays.toString(Chips.getBins(bestLyingOfferType.offer(), game.getBinMaxChips())));
        if (!thereIsBestOfferWithoutMessage) {
            boolean lyingIsBetter = true;
            System.out.println("But there are offers that give " + this.getName() + " a better value than using no message.");

            // choose offer with message
            bestLyingOfferType = bestOffers.get((int) (Math.random() * bestOffers.size()));
            System.out.println("-> Offers that are optimal with message are:");
            for (LyingOfferType something : bestOffers) {
                System.out.println("\t- value=" + Settings.PRINT_DF.format(tmpSelectOfferValue) + ", offer to self=" + something.offer() + "; " +
                        Arrays.toString(Chips.getBins(something.offer(), game.getBinMaxChips())) +
                        ", loc=" + something.loc());
                if (something.loc() == game.getGoalPositionPlayer(this.getName())) {
                    lyingIsBetter = false;
                }
            }
            System.out.println("\t- Chosen is offer to self: " + bestLyingOfferType.offer() + "; " +
                    Arrays.toString(Chips.getBins(bestLyingOfferType.offer(), game.getBinMaxChips())) +
                    ", loc=" + bestLyingOfferType.loc());
            if (lyingIsBetter)
                System.out.println("--- THERE IS A COMBINATION WHERE LYING IS BETTER THAN TELLING THE TRUTH. ---");
        }

        bestOffer = bestLyingOfferType.offer();
        bestLoc = bestLyingOfferType.loc();

        System.out.println(getName() + ": Offer=" + Arrays.toString(Chips.getBins(bestOffer, game.getBinMaxChips())) +
                "; location=" + bestLoc +
                ";\n\tValue without message = " + Settings.PRINT_DF.format(getValue(bestOffer, offerReceived)) +
                ";\n\ttmpSelectOfferValue = " + Settings.PRINT_DF.format(tmpSelectOfferValue));
        if ((utilityFunction[offerReceived] + Settings.EPSILON >= tmpSelectOfferValue) &&
                (utilityFunction[offerReceived] - Settings.EPSILON > utilityFunction[this.chips])) {
            // accept offerReceived as it is better than making a new offer or withdrawing
            System.out.println("ACCEPT");
            bestOffer = offerReceived;
        } else if ((utilityFunction[this.chips] + Settings.EPSILON >= utilityFunction[bestOffer]) &&
                (utilityFunction[this.chips] + Settings.EPSILON >= utilityFunction[offerReceived])) {
            // withdraw from negotiation
            System.out.println("WITHDRAW");
            bestOffer = this.chips;
        } else {  // else, make the best offer with possibly a message
            if (bestLoc != -1) sendMessage(Messages.createLocationMessage(bestLoc));

            double offerValue = getValue(bestOffer, offerReceived);
            if (bestLoc != -1) System.out.println("Offer value (after sending message (" + bestLoc +")): " + Settings.PRINT_DF.format(offerValue));
            if ((offerValue + Settings.EPSILON < tmpSelectOfferValue) || (offerValue - Settings.EPSILON > tmpSelectOfferValue)) {
                System.out.println("!!! WRONG NUMBERS !!! -> " + Settings.PRINT_DF.format(offerValue) + " != " + Settings.PRINT_DF.format(tmpSelectOfferValue));
                for (int i = 0; i < 5; i++) {
                    System.out.println("\tvalue = " + Settings.PRINT_DF.format(getValue(bestOffer, offerReceived)));
                }
            }
        }

        return bestOffer;
    }

    /**
     * Method to add offers with a certain location message
     *
     * @param loc The location to message to the other agent
     */
    private void addOffers(int loc, int offerReceived) {
        double curValue;
        boolean saveHasSentMessage = this.hasSentMessage;

        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            partnerModel.saveBeliefs();
            partnerModel.receiveMessage(Messages.createLocationMessage(loc));
            curValue = getValue(i, offerReceived);  // TODO: what to do when value is higher when sending offer that accepts the offer.
            partnerModel.restoreBeliefs();
            this.setHasSentMessage(saveHasSentMessage);

            processOffer(i, loc, curValue);
        }
    }

    /**
     * Adds offers without a message.
     */
    private void addOffersWithoutMessage(int offerReceived) {
        double curValue;
        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            curValue = getValue(i, offerReceived);
            processOffer(i, -1, curValue);
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
    private void processOffer(int offer, int location, double curValue) {
        if (curValue - Settings.EPSILON > tmpSelectOfferValue) {
            tmpSelectOfferValue = curValue;
            bestOffers = new ArrayList<>();
            bestOffers.add(new LyingOfferType(offer, location));
            thereIsBestOfferWithoutMessage = false;
        } else if ((curValue >= tmpSelectOfferValue - Settings.EPSILON) &&
                (curValue <= tmpSelectOfferValue + Settings.EPSILON)) {
            bestOffers.add(new LyingOfferType(offer, location));
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
    public boolean isCanLie() {
        return canLie;
    }
}