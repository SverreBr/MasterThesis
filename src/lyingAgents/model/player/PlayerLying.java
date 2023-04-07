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
    private List<OfferType> bestOffers;

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
            receiveOffer(offerReceived);
            curOffer = chooseOffer(offerReceived);
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
        return chooseOffer(chips);
    }

    /**
     * Selects the best offer to make as a response to offerReceived. An agent can send an additional message containing
     * a location. All combinations are explored.
     *
     * @param offerReceived the offer made by the other player from the perspective of this agent.
     * @return The best offer as a response to offerReceived.
     */
    public int chooseOffer(int offerReceived) {
        bestOffers = new ArrayList<>();
        OfferType bestLyingOfferType;
        int bestOffer, bestLoc, goalPosition;
        double noMessageOfferValue;

        tmpSelectOfferValue = -Double.MAX_VALUE + Settings.EPSILON;
        bestOffers.add(new OfferType(this.chips, tmpSelectOfferValue, -1));
        addOffersWithoutMessage();  // send no message

        // Choose offer without message if at least as good as with a message.
        List<OfferType> bestOffersWithoutMessage = new ArrayList<>(bestOffers);
        bestLyingOfferType = bestOffers.get((int) (Math.random() * bestOffers.size()));
        noMessageOfferValue = tmpSelectOfferValue;
        thereIsBestOfferWithoutMessage = true;

        if (getOrderToM() > 1) {  // agent models that trading partner beliefs this agent has a goal position
            if (canLie) {
                for (int loc = 0; loc < this.game.getNumberOfGoalPositions(); loc++) {
                    addOffers(loc);
                }
            } else {
                goalPosition = game.getGoalPositionPlayer(this.getName());
                addOffers(goalPosition);
            }
        }

//        System.out.println("-> Offers that are optimal without message:");
//        for (OfferType smt : bestOffersWithoutMessage) {
//            System.out.println("\t- value=" + Settings.PRINT_DF.format(noMessageOfferValue) + "; offer to self=" + smt.getOffer() + "; " +
//                    Arrays.toString(Chips.getBins(smt.getOffer(), game.getBinMaxChips())) +
//                    ", loc=" + smt.getLoc());
//        }
//        System.out.println("\t- Chosen is offer to self: " + bestLyingOfferType.getOffer() + "; " +
//                Arrays.toString(Chips.getBins(bestLyingOfferType.getOffer(), game.getBinMaxChips())));
//        System.out.println("-> Offers that are optimal:");
//        for (OfferType smt : bestOffers) {
//            System.out.println("\t- value=" + Settings.PRINT_DF.format(tmpSelectOfferValue) + "; offer to self=" + smt.getOffer() + "; " +
//                    Arrays.toString(Chips.getBins(smt.getOffer(), game.getBinMaxChips())) +
//                    ", loc=" + smt.getLoc());
//        }
        if (!thereIsBestOfferWithoutMessage) {
//            boolean lyingIsBetter = true;
//            System.out.println("But there are offers that give " + this.getName() + " a better value than using no message.");
//
//            // choose offer with message
            bestLyingOfferType = bestOffers.get((int) (Math.random() * bestOffers.size()));
//            System.out.println("-> Offers that are optimal with message are:");
//            for (OfferType something : bestOffers) {
//                System.out.println("\t- value=" + Settings.PRINT_DF.format(tmpSelectOfferValue) + ", offer to self=" + something.getOffer() + "; " +
//                        Arrays.toString(Chips.getBins(something.getOffer(), game.getBinMaxChips())) +
//                        ", loc=" + something.getLoc());
//                if (something.getLoc() == game.getGoalPositionPlayer(this.getName())) {
//                    lyingIsBetter = false;
//                }
//            }
//            System.out.println("\t- Chosen is offer to self: " + bestLyingOfferType.getOffer() + "; " +
//                    Arrays.toString(Chips.getBins(bestLyingOfferType.getOffer(), game.getBinMaxChips())) +
//                    ", loc=" + bestLyingOfferType.getLoc());
//            if (lyingIsBetter)
//                System.out.println("--- THERE IS A COMBINATION WHERE LYING IS BETTER THAN TELLING THE TRUTH. ---");
        }

        bestOffer = bestLyingOfferType.getOffer();
        bestLoc = bestLyingOfferType.getLoc();

//        System.out.println(getName() + ": Offer=" + Arrays.toString(Chips.getBins(bestOffer, game.getBinMaxChips())) +
//                "; location=" + bestLoc +
//                ";\n\tValue without message = " + Settings.PRINT_DF.format(getValue(bestOffer)) +
//                ";\n\ttmpSelectOfferValue = " + Settings.PRINT_DF.format(tmpSelectOfferValue));
        if ((utilityFunction[offerReceived] + Settings.EPSILON >= tmpSelectOfferValue) &&
                (utilityFunction[offerReceived] - Settings.EPSILON > utilityFunction[this.chips])) {
            // accept offerReceived as it is better than making a new offer or withdrawing
//            System.out.println("ACCEPT");
//            if (bestOffer == offerReceived) System.out.println("!!! BEST OFFER IS ALREADY OFFER RECEIVED !!!");
            bestOffer = offerReceived;
        } else if ((utilityFunction[this.chips] + Settings.EPSILON >= tmpSelectOfferValue) &&
                (utilityFunction[this.chips] + Settings.EPSILON >= utilityFunction[offerReceived])) {
            // withdraw from negotiation
//            System.out.println("WITHDRAW");
//            if (bestOffer == this.chips) System.out.println("BEST OFFER IS ALREADY CURRENT CHIPS");
            bestOffer = this.chips;
        } else {  // else, make the best offer with possibly a message
            if (bestLoc != -1) sendMessage(Messages.createLocationMessage(bestLoc));
        }

        return bestOffer; // TODO: Check this...
    }

    /**
     * Method to add offers with a certain location message
     *
     * @param loc The location to message to the other agent
     */
    private void addOffers(int loc) {
        double curValue;
        boolean saveHasSentMessage = this.hasSentMessage;

        if (getOrderToM() > 0) {
            for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
                partnerModel.saveBeliefs();
                partnerModel.receiveMessage(Messages.createLocationMessage(loc));
                curValue = getValue(i);
                partnerModel.restoreBeliefs();
                this.setHasSentMessage(saveHasSentMessage);
                processOffer(i, curValue, loc);
            }
        }
    }

    /**
     * Adds offers without a message.
     */
    private void addOffersWithoutMessage() {
        double curValue;
        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            curValue = getValue(i);
            processOffer(i, curValue, -1);
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
    private void processOffer(int offer, double curValue, int location) {
        if (curValue - Settings.EPSILON > tmpSelectOfferValue) {
            tmpSelectOfferValue = curValue;
            bestOffers = new ArrayList<>();
            bestOffers.add(new OfferType(offer, curValue, location));
            thereIsBestOfferWithoutMessage = false;
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
    public boolean isCanLie() {
        return canLie;
    }
}