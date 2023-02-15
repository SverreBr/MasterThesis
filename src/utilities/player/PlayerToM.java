package utilities.player;

import utilities.Game;

import java.util.Arrays;

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
    private double[] savedLocationBeliefs;

    /**
     * The model of the partner
     */
    private final PlayerToM partnerModel;

    /**
     * The model of itself for using lower orders of mind
     */
    private final PlayerToM selfModel;

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
    private boolean confidenceLocked;

    /**
     * Constructor
     *
     * @param playerName name of the agent
     * @param game       the model of the game
     */
    public PlayerToM(String playerName, Game game, int orderToM, double learningSpeed) {
        super(playerName, game, learningSpeed);
        this.orderToM = orderToM;

        this.locationBeliefs = new double[this.game.getNumberOfGoalPositions()];

        if (this.orderToM > 0) {
            selfModel = new PlayerToM("", game, orderToM - 1, learningSpeed);
            partnerModel = new PlayerToM("", game, orderToM - 1, learningSpeed);
            partnerModel.setConfidenceLockedTo(true);
        } else {
            selfModel = null;
            partnerModel = null;
        }
    }

    /**
     * Resets the agent for a full new round of play, without any learned behaviour.
     *
     * @param chipsSelf       The chips to himself
     * @param chipsOther      The chips to the other player
     * @param utilityFunction The utility function for this player.
     */
    public void reset(int chipsSelf, int chipsOther, int[] utilityFunction) {
        super.reset(chipsSelf, chipsOther, utilityFunction);
        if (this.orderToM > 0) {
            Arrays.fill(locationBeliefs, 1.0 / locationBeliefs.length);
            selfModel.reset(chipsSelf, chipsOther, utilityFunction);
            partnerModel.reset(chipsOther, chipsSelf, utilityFunction);
            confidence = 1.0;  // starting confidence
        }
    }

    /**
     * Initializes the agent for a new round, but learning from other games is kept.
     *
     * @param chipsSelf       The chips to himself
     * @param chipsOther      The chips to the other player
     * @param utilityFunction The utility function for this player
     */
    public void initNewRound(int chipsSelf, int chipsOther, int[] utilityFunction) {
        super.initNewRound(chipsSelf, chipsOther, utilityFunction);
        if (orderToM > 0) {
            Arrays.fill(locationBeliefs, 1.0 / locationBeliefs.length);
            selfModel.initNewRound(chipsSelf, chipsOther, utilityFunction);
            partnerModel.initNewRound(chipsOther, chipsSelf, utilityFunction);
            confidence = 1.0;  // starting confidence
        }
    }

    /**
     * Makes an offer when agent receives offerReceived
     *
     * @param offerReceived the offer made by Player p from the perspective of this agent.
     *                      That is, if accepted, the agent gets offer
     * @return the offer that this agent makes as a response to offerReceived.
     */
    public int makeOffer(int offerReceived) {
        int curOffer;

        if (offerReceived < 0) {
            // No offer to receive
            curOffer = selectInitialOffer();
        } else {
            receiveOffer(offerReceived);
            curOffer = selectOffer(offerReceived);
        }
        sendOffer(curOffer);
        return this.game.flipOffer(curOffer); // TODO: (SELF) maybe switch the return to the player itself.
    }

    /**
     * Makes an initial offer. In this case, it is as if the other agent made the offer to keep chips.
     *
     * @return the offer that this agent makes as an initial offer
     */
    public int selectInitialOffer() {
        return selectOffer(chips);
    }

    /**
     * Selects an offer as a response to offerReceived
     *
     * @param offerReceived the offer made by the other player from the perspective of this agent.
     * @return The offer offered to the other player from the perspective of this agent.
     * That is, if accepted, this agent gets the offer.
     */
    public int selectOffer(int offerReceived) {
        int bestOffer;
        double curValue, tmpSelectOfferValue = 0.0;

        bestOffer = this.chips;
        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            curValue = getValue(i);
            if (curValue > tmpSelectOfferValue) {  // TODO: selecting a best offer, why not randomize here?  -->  Zou een list kunnen zijn.
                tmpSelectOfferValue = curValue;
                bestOffer = i;
            }
        }
        if (utilityFunction[offerReceived] - utilityFunction[chips] >= tmpSelectOfferValue ||
                utilityFunction[offerReceived] >= utilityFunction[bestOffer]) {
            // offerReceived is better than making new offer and TODO: what does this first condition exactly mean? E.g., why use tmpSelectOfferValue?
            tmpSelectOfferValue = utilityFunction[offerReceived] - utilityFunction[chips];
            bestOffer = offerReceived;
        }
        if (tmpSelectOfferValue < 0 || utilityFunction[bestOffer] < utilityFunction[chips]) {
            // TODO: what is the difference between these two conditions?  --> tweede kan weg
            bestOffer = chips;
        }
        return bestOffer;
    }

    /**
     * Calculates the expected value of an offer
     *
     * @param offerToSelf offer from the perspective of this agent. That is, if accepted, this agent gets offerToSelf
     * @return the expected value of making this offer.
     */
    protected double getValue(int offerToSelf) {
        int loc, offerToOther;
        double curValue = 0.0;
        if (this.orderToM == 0) {
            // ToM0 uses only expected value
            return getExpectedValue(offerToSelf);
        }
        if (this.confidence > 0 || this.confidenceLocked) {
            offerToOther = this.game.flipOffer(offerToSelf);
            this.partnerModel.saveBeliefs();
            this.partnerModel.receiveOffer(offerToOther);
            for (loc = 0; loc < this.game.getNumberOfGoalPositions(); loc++) {
                this.partnerModel.utilityFunction = this.game.getUtilityFunction(loc);
                if (this.locationBeliefs[loc] > 0.0) {
                    curValue += this.locationBeliefs[loc] * getLocationValue(offerToSelf, offerToOther, loc);
                }
            }
            this.partnerModel.restoreBeliefs();
        }
        if (this.confidence >= 1 || confidenceLocked) {
            // fully confident in own theory of mind capabilities
            return curValue;
        }
        // recursion of theory of mind
        return curValue * confidence + (1 - confidence) * selfModel.getValue(offerToSelf);

    }

    /**
     * Gets the value of making an offer, given that the goal location of the partner is currentLocation.
     * This value is multiplied by the belief of the location actually being the goal location
     *
     * @param offerToSelf     offer to agent self
     * @param offerToOther    offer to the other agent
     * @param currentLocation the assumed goal location of the partner
     * @return the value associated to
     * TODO: why use change in utility and not expected utility itself in these functions?
     */
    protected double getLocationValue(int offerToSelf, int offerToOther, int currentLocation) {
        int response = partnerModel.selectOffer(offerToOther);
        double curValue = 0.0;
        if (response == offerToOther) {
            // Partner accepts, probability of location times utility gain of making offer.
            curValue += locationBeliefs[currentLocation] * (utilityFunction[offerToSelf] - utilityFunction[chips] - 1);
        } else if (response != partnerModel.chips) {
            // Offer is not equal to own chips, so new offer has been made
            // Probability of location times utility gain of the modelled counter offer being made
            response = game.flipOffer(response);
            curValue += locationBeliefs[currentLocation] * (Math.max(0, utilityFunction[response] - utilityFunction[chips] - 1) - 1);
        } // If neither case is satisfied, partner terminates negotiations, resulting in value 0.
        return curValue;
    }

    /**
     * Deals with updating beliefs since the partner made an offer
     *
     * @param offerReceived the offer received.
     */
    protected void receiveOffer(int offerReceived) {
        int inverseOffer;
        super.receiveOffer(offerReceived);
        if (this.orderToM > 0) {
            updateLocationBeliefs(offerReceived);
            inverseOffer = this.game.flipOffer(offerReceived);
            // Update partner model for the fact that they sent the offer that was just received
            this.partnerModel.sendOffer(inverseOffer);
            this.selfModel.receiveOffer(offerReceived);
        }
    }

    /**
     * Update beliefs as if offer were to be rejected. If offer will not be rejected, the beliefs are updated as they were
     *
     * @param offerToSelf The offer this agent makes with respect to himself.
     */
    protected void sendOffer(int offerToSelf) {
        int inverseOffer;
        super.sendOffer(offerToSelf);
        if (orderToM > 0) {
            inverseOffer = game.flipOffer(offerToSelf);
            // Update the partner model for receiving the offer that was made
            partnerModel.receiveOffer(inverseOffer);
            selfModel.sendOffer(offerToSelf);
        }
    }

    /**
     * Updates the beliefs of each location.
     *
     * @param offerReceived The offer received by the partner
     */
    private void updateLocationBeliefs(int offerReceived) {
        int loc, partnerAlternative, offerPartnerChips;
        double sumAll, maxExpVal, curExpVal, accuracyRating, newBelief;

        offerPartnerChips = game.flipOffer(offerReceived);
        sumAll = 0.0;
        accuracyRating = 0.0;
        for (loc = 0; loc < game.getNumberOfGoalPositions(); loc++) {
            partnerModel.utilityFunction = game.getUtilityFunction(loc);
            partnerAlternative = partnerModel.selectOffer(0); // 0 since worst possible offer
            // Agent's guess for partner's best option given location l
            curExpVal = partnerModel.getValue(offerPartnerChips);
            // Agent's guess for partner's value of offerReceived

            // TODO: Ask why curExpVal < 0?? --> to avoid else??
            if (partnerModel.utilityFunction[offerPartnerChips] > partnerModel.utilityFunction[partnerModel.chips] ||
                    curExpVal < 0) {  // Partner does not have a better alternative
                // Given loc, offerReceived gives the partner a higher score than the initial situation and withdrawing
                maxExpVal = partnerModel.getValue(partnerAlternative);
                if (maxExpVal > -1) {  // TODO: why -1 and not 0?
                    newBelief = (1 + curExpVal) / (1 + maxExpVal);
                    locationBeliefs[loc] *= Math.max(0.0, Math.min(1.0, newBelief));
                    accuracyRating += locationBeliefs[loc];
                } // else: No offer is expected as the maximum value is -1.
            } else {
                // Making the offer "offerReceived" is not rational, the score would decrease.
                // This is considered to be impossible.
                locationBeliefs[loc] = 0;
            }
            sumAll += locationBeliefs[loc];
        }
        if (sumAll > 0) {
            sumAll = 1.0 / sumAll;
            for (loc = 0; loc < locationBeliefs.length; loc++) {
                locationBeliefs[loc] *= sumAll;
            }
        }
        if (!confidenceLocked) {
            double learningSpeed = getLearningSpeed();
            confidence = Math.min(1.0, Math.max(0.0, (1 - learningSpeed) * confidence + learningSpeed * accuracyRating));
            if (this.orderToM > 1) {
                selfModel.updateLocationBeliefs(offerReceived);
            }
        }
    }

    /**
     * Saves beliefs for (nested) hypothetical beliefs
     */
    public void saveBeliefs() {
        super.saveBeliefs();
        if (orderToM > 0) {
            savedLocationBeliefs = locationBeliefs.clone();
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
            locationBeliefs = savedLocationBeliefs.clone();
            partnerModel.restoreBeliefs();
            selfModel.restoreBeliefs();
        }
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
}
