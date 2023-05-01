package lyingAgents.model.player;

import lyingAgents.model.Game;
import lyingAgents.utilities.Messages;
import lyingAgents.utilities.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PlayerToM: class for the theory of mind of the agent
 */
public class PlayerToM extends Player {

    /**
     * Consists of the beliefs of this agent about the possible goal locations of the other agent.
     * The length is equal to the amount of possible goal positions.
     */
    private double[] locationBeliefs;

    /**
     * To save beliefs of the location.
     */
    private final double[][] savedLocationBeliefs;

    /**
     * The model of the partner
     */
    protected final PlayerToM partnerModel;

    /**
     * The model of itself for using lower orders of mind
     */
    protected final PlayerToM selfModel;

    /**
     * The order of theory of mind
     */
    private final int orderToM;

    /**
     * Confidence in orderToM-1 of the opponent
     */
    private double confidence;

    /**
     * Lock the confidence in using their theory of mind capability of this agent
     */
    protected boolean confidenceLocked = false;

    /**
     * Boolean value for whether this agent received a message or not
     */
    protected boolean receivedMessage = false;

    /**
     * Boolean value for whether this agent has sent a message.
     */
    protected boolean hasSentMessage = false;

    /**
     * Location beliefs of this agent with respect to the trading partner without updates of potential message.
     * The agent falls back to these beliefs when the agent decides that it cannot trust the trading partner.
     * other agent.
     */
    private double[] locationBeliefsWithoutMessage;

    /**
     * The saved locationBeliefsWithoutMessage. This stores the mentioned field when saveBeliefs is called.
     */
    private final double[][] savedLocationBeliefsWithoutMessage;

    /**
     * @param playerName      name of the player
     * @param game            game model
     * @param orderToM        order of theory of mind
     * @param learningSpeed   learning speed
     * @param chipsSelf       offer that are for this player
     * @param chipsOther      offer that are for the other player
     * @param utilityFunction utility function for this player
     */
    public PlayerToM(String playerName, Game game, int orderToM, double learningSpeed, int chipsSelf, int chipsOther, int[] utilityFunction) {
        super(playerName, game, learningSpeed, chipsSelf, utilityFunction, (orderToM == 0));
        this.orderToM = orderToM;

        if (this.orderToM > 0) {
            this.locationBeliefs = new double[this.game.getNumberOfGoalPositions()];
            this.savedLocationBeliefs = new double[Settings.SAVE_NUMBER][this.game.getNumberOfGoalPositions()];
            this.savedLocationBeliefsWithoutMessage = new double[Settings.SAVE_NUMBER][this.game.getNumberOfGoalPositions()];
            this.locationBeliefsWithoutMessage = new double[this.game.getNumberOfGoalPositions()];

            selfModel = new PlayerToM("selfModel_" + playerName, game, orderToM - 1, learningSpeed, chipsSelf, chipsOther, utilityFunction);
            partnerModel = new PlayerToM("partnerModel_" + playerName, game, orderToM - 1, learningSpeed, chipsOther, chipsSelf, utilityFunction);
            partnerModel.setConfidenceLockedTo(true);
        } else {
            selfModel = null;
            partnerModel = null;
            this.locationBeliefs = null;
            this.savedLocationBeliefs = null;
            this.savedLocationBeliefsWithoutMessage = null;
            this.locationBeliefsWithoutMessage = null;
        }

        initNegotiationRound(chipsSelf, chipsOther, utilityFunction);
    }

    /**
     * Initializes the agent for a new round, but learning from other games is kept.
     *
     * @param chipsSelf       The offer to himself
     * @param chipsOther      The offer to the other player
     * @param utilityFunction The utility function for this player
     */
    @Override
    public void initNegotiationRound(int chipsSelf, int chipsOther, int[] utilityFunction) {
        super.initNegotiationRound(chipsSelf, chipsOther, utilityFunction);
        this.receivedMessage = false;
        this.hasSentMessage = false;
        if (orderToM > 0) {
            Arrays.fill(locationBeliefs, 1.0 / locationBeliefs.length);
            Arrays.fill(locationBeliefsWithoutMessage, -1);
            for (int i = 0; i < Settings.SAVE_NUMBER; i++) {
                Arrays.fill(savedLocationBeliefs[i], -1);
                Arrays.fill(savedLocationBeliefsWithoutMessage[i], -1);
            }
            selfModel.initNegotiationRound(chipsSelf, chipsOther, utilityFunction);
            partnerModel.initNegotiationRound(chipsOther, chipsSelf, utilityFunction);
            confidence = 1.0;  // starting confidence
        }
    }

    /**
     * Gives the agent's counter-offer for a specific offer
     *
     * @param offerReceived the offer made by Player p from the perspective of this agent.
     *                      That is, if accepted, the agent gets offer
     * @return the counter-offer to Player p from the perspective of this agent.
     * That is, if accepted, this player gets the returned value.
     */
    public int makeOffer(int offerReceived) {
        int curOffer;

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
        return offerType.getOffer();
    }

    /**
     * Selects an offer as a response to offerReceived
     *
     * @param offerReceived the offer made by the other player from the perspective of this agent.
     * @return The offer offered to the other player from the perspective of this agent.
     * That is, if accepted, this agent gets the offer.
     */
    public List<OfferType> selectBestOffers(int offerReceived) {
        List<OfferType> bestOffers = new ArrayList<>();
        double curValue, tmpSelectOfferValue;

        tmpSelectOfferValue = -Double.MAX_VALUE + Settings.EPSILON;
        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            curValue = getValue(i);
            if (curValue - Settings.EPSILON > tmpSelectOfferValue) {
                tmpSelectOfferValue = curValue;
                bestOffers = new ArrayList<>();
                bestOffers.add(new OfferType(i, tmpSelectOfferValue, Settings.ID_NO_LOCATION));
            } else if ((curValue >= tmpSelectOfferValue - Settings.EPSILON) &&
                    (curValue <= tmpSelectOfferValue + Settings.EPSILON)) {
                bestOffers.add(new OfferType(i, tmpSelectOfferValue, Settings.ID_NO_LOCATION));
            }
        }

        return checkPossibleOffers(offerReceived, bestOffers, tmpSelectOfferValue);
    }

    protected List<OfferType> checkPossibleOffers(int offerReceived, List<OfferType> bestOffers, double tmpSelectOfferValue) {
        if ((offerReceived != Settings.ID_NO_OFFER) &&
                (utilityFunction[offerReceived] + Settings.EPSILON >= tmpSelectOfferValue) &&
                (utilityFunction[offerReceived] - Settings.EPSILON > utilityFunction[this.chips])) {
            // accept offerReceived as it is better than making a new offer or withdrawing
            bestOffers = new ArrayList<>();
            bestOffers.add(new OfferType(Settings.ID_ACCEPT_OFFER, utilityFunction[offerReceived], Settings.ID_NO_LOCATION));
        } else if ((utilityFunction[this.chips] + Settings.EPSILON >= tmpSelectOfferValue) &&
                ((offerReceived == Settings.ID_NO_OFFER) || (utilityFunction[this.chips] + Settings.EPSILON >= utilityFunction[offerReceived]))) {
            // withdraw from negotiation
            bestOffers = new ArrayList<>();
            bestOffers.add(new OfferType(Settings.ID_WITHDRAW_NEGOTIATION, utilityFunction[this.chips], Settings.ID_NO_LOCATION));
        } // else, make (one of) the best offer(s)
        return bestOffers;
    }


    /**
     * Calculates the expected value of an offer
     *
     * @param makeOfferToSelf offer from the perspective of this agent. That is, if accepted, this agent gets makeOfferToSelf
     * @return the expected value of making this offer.
     */
    protected double getValue(int makeOfferToSelf) {
        int loc, offerToOther;
        double curValue = 0.0;

        if (orderToM == 0) {
            // ToM0 uses only expected value
            return getExpectedValue(makeOfferToSelf);
        }
        if ((confidence - Settings.EPSILON > 0) || confidenceLocked) {
            offerToOther = game.flipOffer(makeOfferToSelf);
            partnerModel.saveBeliefs();
            partnerModel.receiveOffer(offerToOther);
            for (loc = 0; loc < game.getNumberOfGoalPositions(); loc++) {
                if (locationBeliefs[loc] - Settings.EPSILON > 0.0) {
                    partnerModel.utilityFunction = game.getUtilityFunction(loc);
                    curValue += locationBeliefs[loc] * getLocationValue(makeOfferToSelf, offerToOther);
                }
            }
            partnerModel.restoreBeliefs();
        }
        if ((confidence + Settings.EPSILON >= 1) || confidenceLocked) {
            // fully confident in own theory of mind capabilities
            return curValue;
        }

        // recursion of theory of mind
        return curValue * confidence + (1 - confidence) * selfModel.getValue(makeOfferToSelf);
    }

    /**
     * Gets the value of making an offer.
     *
     * @param offerToSelf  offer to agent self
     * @param offerToOther offer to the other agent
     * @return the value associated to making the offer
     */
    private double getLocationValue(int offerToSelf, int offerToOther) {
        List<OfferType> expectedResponses = partnerModel.selectBestOffers(offerToOther);
        double chance = 1.0 / expectedResponses.size();
        double curValue, totValue;
        int someOffer;

        totValue = 0.0;
        for (OfferType expectedResponse : expectedResponses) {
            if (expectedResponse.getOffer() == Settings.ID_ACCEPT_OFFER) {
                // Partner accepts.
                curValue = utilityFunction[offerToSelf] + Settings.SCORE_NEGOTIATION_STEP;
            } else if (expectedResponse.getOffer() == Settings.ID_WITHDRAW_NEGOTIATION) {
                // Offer equals partner its offer, so partner has withdrawn from negotiation
                curValue = utilityFunction[this.chips] + Settings.SCORE_NEGOTIATION_STEP;
            } else {
                // Offer is not equal to partner its offer, so new offer has been made
                someOffer = game.flipOffer(expectedResponse.getOffer());
                curValue = Math.max(utilityFunction[someOffer], utilityFunction[this.chips]) + 2*Settings.SCORE_NEGOTIATION_STEP;
                // minus one for current offer and one for offer from partner
            }
            totValue += chance * curValue;
        }

        return totValue;
    }

    /**
     * Deals with updating beliefs since the partner made an offer
     *
     * @param offerReceived the offer received.
     */
    @Override
    protected void receiveOffer(int offerReceived) {
        int inverseOffer;
        if (this.orderToM == 0) {
            super.receiveOffer(offerReceived);
        } else {
            updateLocationBeliefs(offerReceived);

            if (receivedMessage && !(getSumLocationBeliefs() - Settings.EPSILON > 0)) { // offer is not consistent with what is offered...
                restoreLocationBeliefsDueToUnbelievedMessage();
                if (Game.DEBUG && (getName().equals(Settings.INITIATOR_NAME) || getName().equals(Settings.RESPONDER_NAME))) {
                    System.out.println(getName() + " does not believe trading partner ###########################");
                    addMessage("(Agent doesn't believe trading partner.)", false);
                }
            }
            selfModel.receiveOffer(offerReceived);

            // Update partner lyingAgents.model for the fact that they had send the offer that was just received
            inverseOffer = this.game.flipOffer(offerReceived);
            partnerModel.sendOffer(inverseOffer);
        }
    }

    private double getSumLocationBeliefs() {
        double sum = 0.0;
        for (int loc = 0; loc < game.getNumberOfGoalPositions(); loc++) {
            sum += locationBeliefs[loc];
        }
        return sum;
    }

    /**
     * Update beliefs as if offer were to be rejected. If offer will not be rejected, the beliefs are updated as they were
     *
     * @param offerToSelf The offer this agent makes with respect to himself.
     */
    @Override
    protected void sendOffer(int offerToSelf) {
        int inverseOffer;
        if (orderToM == 0) {
            super.sendOffer(offerToSelf);
        } else {
            selfModel.sendOffer(offerToSelf);

            // Update the partner lyingAgents.model for receiving the offer that was made
            inverseOffer = game.flipOffer(offerToSelf);
            partnerModel.receiveOffer(inverseOffer);
        }
    }

    /**
     * Updates the beliefs of each location.
     *
     * @param offerReceived The offer received by the partner
     */
    private void updateLocationBeliefs(int offerReceived) {
        int loc, flippedOfferReceived, utilityOffer, modeledPartnerAlternative;
        double sumAll, maxExpVal, curExpVal, accuracyRating, newBelief, sumAllSavedBeliefs;

        flippedOfferReceived = game.flipOffer(offerReceived);
        sumAll = 0.0;
        sumAllSavedBeliefs = 0.0;
        accuracyRating = 0.0;
        for (loc = 0; loc < game.getNumberOfGoalPositions(); loc++) {
            partnerModel.utilityFunction = game.getUtilityFunction(loc);
            utilityOffer = partnerModel.utilityFunction[flippedOfferReceived] + Settings.SCORE_NEGOTIATION_STEP;
            if (utilityOffer - Settings.EPSILON > partnerModel.utilityFunction[partnerModel.chips]) {
                // Given loc, offerReceived gives the partner a higher score than the initial situation
                modeledPartnerAlternative = partnerModel.chooseOffer(Settings.ID_NO_OFFER); // 0 since worst possible offer

                if (modeledPartnerAlternative == Settings.ID_WITHDRAW_NEGOTIATION) {
                    maxExpVal = partnerModel.utilityFunction[partnerModel.chips];
                } else {
                    // Agent's guess for partner's best option given location l
                    maxExpVal = partnerModel.getValue(modeledPartnerAlternative);
                }
                curExpVal = partnerModel.getValue(flippedOfferReceived);

                // Agent's guess for partner's value of offerReceived
                if (maxExpVal - Settings.EPSILON > utilityOffer) {
                    newBelief = Math.max(0.0, Math.min(1.0, (1 + curExpVal) / (1 + maxExpVal)));
                    locationBeliefs[loc] *= newBelief;
                    if (this.receivedMessage) locationBeliefsWithoutMessage[loc] *= newBelief;
                    accuracyRating += locationBeliefs[loc];
                } // else: No offer is expected as the maxvalue is not greater than withdrawing from negotiation. Hence, partnerModel is incorrect.

            } else {
                // Making the offer "offerReceived" is not rational, the score would decrease.
                // This is considered to be impossible; so set belief to zero.
                locationBeliefs[loc] = 0;
                if (this.receivedMessage) locationBeliefsWithoutMessage[loc] = 0;
            }
            sumAll += locationBeliefs[loc];
            if (receivedMessage) sumAllSavedBeliefs += locationBeliefsWithoutMessage[loc];
        }
        if (sumAll - Settings.EPSILON > 0) {
            sumAll = 1.0 / sumAll;
            for (loc = 0; loc < locationBeliefs.length; loc++) {
                locationBeliefs[loc] *= sumAll;
            }
        }
        if (receivedMessage && (sumAllSavedBeliefs - Settings.EPSILON > 0)) {
            sumAllSavedBeliefs = 1.0 / sumAllSavedBeliefs;
            for (loc = 0; loc < locationBeliefs.length; loc++) {
                locationBeliefsWithoutMessage[loc] *= sumAllSavedBeliefs;
            }
        }

        if (!confidenceLocked) {
            double learningSpeed = getLearningSpeed();
            confidence = Math.min(1.0, Math.max(0.0, (1 - learningSpeed) * confidence + learningSpeed * accuracyRating));
        }
    }

    /**
     * Saves beliefs for (nested) hypothetical beliefs
     */
    public void saveBeliefs() {
        super.saveBeliefs();
        if (orderToM > 0) {
            savedLocationBeliefs[saveCount - 1] = locationBeliefs.clone();
            savedLocationBeliefsWithoutMessage[saveCount - 1] = locationBeliefsWithoutMessage.clone();
            partnerModel.saveBeliefs();
            selfModel.saveBeliefs();
        }
    }

    /**
     * Restore previous pushed back beliefs
     */
    public void restoreBeliefs() {
        super.restoreBeliefs();
        if (orderToM > 0) {
            locationBeliefs = savedLocationBeliefs[saveCount].clone();
            locationBeliefsWithoutMessage = savedLocationBeliefsWithoutMessage[saveCount].clone();
            partnerModel.restoreBeliefs();
            selfModel.restoreBeliefs();
        }
    }

    @Override
    public void processOfferAccepted(int offerMade) {
        if (orderToM == 0) super.processOfferAccepted(offerMade);
        setNewChips(offerMade);
    }

    ///////////////////////////////
    //--- SETTERS AND GETTERS ---//
    ///////////////////////////////

    /**
     * Sets the learning speed of this agent and the model of the partner and itself.
     *
     * @param newLearningSpeed The new learning speed of the agent
     */
    public void setLearningSpeed(double newLearningSpeed) {
        super.setLearningSpeed(newLearningSpeed);
        if (orderToM > 0) {
            selfModel.setLearningSpeed(newLearningSpeed);
            partnerModel.setLearningSpeed(newLearningSpeed);
        }
    }

    /**
     * Getter for the order of theory of mind of this agent
     *
     * @return The order of theory of mind
     */
    public int getOrderToM() {
        return this.orderToM;
    }

    /**
     * Sets the confidence of this agent locked or not
     */
    public void setConfidenceLockedTo(boolean confidenceLocked) {
        this.confidenceLocked = confidenceLocked;
    }

    /**
     * Getter for the confidence of this agent in its theory of mind
     *
     * @return Confidence in its theory of mind
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Getter for the initial points of this agent
     *
     * @return Initial points of this agent
     */
    public int getInitialPoints() {
        return utilityFunction[this.getInitialChips()];
    }

    /**
     * Gets the location beliefs of agent model of its orderTom order theory of mind.
     *
     * @param orderToM The order of theory of mind of which to get the location beliefs from
     * @return An array of location beliefs
     */
    public double[] getLocationBeliefs(int orderToM) {
        if (this.orderToM <= 0) {
            return null;
        }
        if (this.orderToM == orderToM) {
            return locationBeliefs;
        }
        return selfModel.getLocationBeliefs(orderToM);
    }

    /**
     * Gets the location beliefs without influence of a message of this agents model of its orderTom order theory of mind.
     *
     * @param orderToM The order of theory of mind of which to get the location beliefs from
     * @return An array of location beliefs
     */
    public double[] getLocationBeliefsWithoutMessage(int orderToM) {
        if (this.orderToM <= 0) {
            return null;
        }
        if (this.orderToM == orderToM) {
            return locationBeliefsWithoutMessage;
        }
        return selfModel.getLocationBeliefsWithoutMessage(orderToM);
    }

    /**
     * Gets the location beliefs of the partner model of its orderTom order theory of mind.
     *
     * @param orderToMSelf The order of theory of mind this agent must have.
     * @return An array of location beliefs
     */
    public double[] getPartnerModelLocationBeliefs(int orderToMSelf) {
        if (orderToMSelf < 2) { // Partner lyingAgents.model only has location beliefs when it is ToM > 1
            return null;
        }
        if (orderToMSelf == orderToM) {
            return partnerModel.getLocationBeliefs(orderToMSelf - 1);
        }
        return selfModel.getPartnerModelLocationBeliefs(orderToMSelf);
    }

    /**
     * Gets the location beliefs of the partner model without influence of a message of its orderTom order theory of mind
     *
     * @param orderToMSelf The order of theory of mind this agent must have.
     * @return An array of location beliefs
     */
    public double[] getPartnerModelLocationBeliefsWithoutMessage(int orderToMSelf) {
        if (orderToMSelf < 2) { // Partner lyingAgents.model only has location beliefs when it is ToM > 1
            return null;
        }
        if (orderToMSelf == orderToM) {
            return partnerModel.getLocationBeliefsWithoutMessage(orderToMSelf - 1);
        }
        return selfModel.getPartnerModelLocationBeliefsWithoutMessage(orderToMSelf);
    }


    /////////////////
    ////  LYING  ////
    /////////////////

    /**
     * Method called when the agent receives a message with a location. Agent checks if it is going to believe the agent.
     * It does not believe the agent when
     *
     * @param location The location that the trading partner announced
     */
    private void receiveLocationMessage(int location) {
        if (this.orderToM > 0) { // Models goal location of trading partner
            if (!this.receivedMessage) { // First message received
                double savedLocationBelief = locationBeliefs[location];
                storeLocationBeliefsDueToBelievedMessage();
                for (int loc = 0; loc < game.getNumberOfGoalPositions(); loc++) {
                    locationBeliefs[loc] = 0.0;
                }
                if (savedLocationBelief - Settings.EPSILON > 0.0) locationBeliefs[location] = 1.0;
            } else {  // already received a message
                if (locationBeliefs[location] - Settings.EPSILON <= 0.0) {
                    for (int loc = 0; loc < game.getNumberOfGoalPositions(); loc++) {
                        locationBeliefs[loc] = 0.0;
                    }
                } // else, agent can still believe trading partner, or already disbelieved the trading partner.
            }
        } else {
            decreaseColorBeliefMessage(location);
        }
        this.receivedMessage = true;
    }


    /**
     * Method that is called when the agent receives a message.
     *
     * @param message The message to be handled by the player.
     */
    public void receiveMessage(String message) {
        int loc;
        String messageType = Messages.getMessageType(message);
        if (messageType.equals(Messages.LOCATION_MESSAGE)) {
            loc = Messages.getLocationFromMessage(message);
            receiveLocationMessage(loc);
        }
        if (orderToM > 0) { // Agent has selfModel and partnerModel
            selfModel.receiveMessage(message);
            partnerModel.sendMessage(message);
        }
    }

    /**
     * Method called when agent sends a message.
     *
     * @param message The message to send to the other agent
     */
    public void sendMessage(String message) {
        this.hasSentMessage = true;

        if (orderToM > 0) {
            partnerModel.receiveMessage(message);
            selfModel.sendMessage(message);
        }
    }

    /**
     * Sets the field hasSentMessage
     *
     * @param hasSentMessage True if the field hasSentMessage must be true, false otherwise
     */
    public void setHasSentMessage(boolean hasSentMessage) {
        this.hasSentMessage = hasSentMessage;
        if (orderToM > 0) {
            this.selfModel.setHasSentMessage(hasSentMessage);
            this.partnerModel.setHasReceivedMessage(hasSentMessage);
        }
    }

    /**
     * Sets the field receivedMessage
     *
     * @param hasReceivedMessage True if the field receivedMessage must be true, false otherwise
     */
    public void setHasReceivedMessage(boolean hasReceivedMessage) {
        this.receivedMessage = hasReceivedMessage;
        if (orderToM > 0) {
            this.selfModel.setHasReceivedMessage(hasReceivedMessage);
            this.partnerModel.setHasSentMessage(hasReceivedMessage);
        }
    }

    /**
     * Restores the locationBeliefs to the location beliefs without influence of a message
     */
    private void restoreLocationBeliefsDueToUnbelievedMessage() {
        locationBeliefs = locationBeliefsWithoutMessage.clone();
    }

    /**
     * Stores the location beliefs in location beliefs without a message
     */
    private void storeLocationBeliefsDueToBelievedMessage() {
        locationBeliefsWithoutMessage = locationBeliefs.clone();
    }

    public boolean getHasReceivedMessage() {
        return receivedMessage;
    }

    public boolean getHasSentMessage() {
        return hasSentMessage;
    }
}
