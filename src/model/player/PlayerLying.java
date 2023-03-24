package model.player;

import model.Game;
import utilities.Chips;
import utilities.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerLying extends PlayerToM {

    private final boolean canLie;

    private List<LyingOfferType> bestOffers;

    private double tmpSelectOfferValue;
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

    public int makeOffer(int offerReceived) {
        int curOffer;

        if (offerReceived < 0) {
            curOffer = selectInitialOffer();
        } else {
            receiveOffer(offerReceived);
            curOffer = selectOffer(offerReceived);
        }
        sendOffer(curOffer);
        return curOffer;
    }

    public int selectInitialOffer() {
        return selectOffer(chips);
    }

    public int selectOffer(int offerReceived) {
        bestOffers = new ArrayList<>();
        LyingOfferType bestLyingOfferType;
        int bestOffer, bestLoc, goalPosition;
        double noMessageOfferValue;

        bestOffers.add(new LyingOfferType(this.chips, -1));  // withdraw from negotiation as basis
        tmpSelectOfferValue = utilityFunction[this.chips];
        addOffers(-1);  // send no message

        // Choose offer without message if at least as good as with a message.
        List<LyingOfferType> bestOffersWithoutMessage = new ArrayList<>(bestOffers);
        bestLyingOfferType = bestOffers.get((int) (Math.random() * bestOffers.size()));
        noMessageOfferValue = tmpSelectOfferValue;
        thereIsBestOfferWithoutMessage = true;

        if (getOrderToM() > 1) {  // agent models that trading partner beliefs this agent has a goal position
            if (canLie) {
                for (int loc = 0; loc < this.game.getNumberOfGoalPositions(); loc++) {
                    addOffers(loc);
                }
            }
            else {
                goalPosition = game.getGoalPositionPlayer(this.getName());
                addOffers(goalPosition);
                partnerModel.restoreBeliefs();
            }
        }
        if (!thereIsBestOfferWithoutMessage) {
            boolean lyingIsBetter = true;
            System.out.println("\nThere are offers that give " + this.getName() + " a better value than using no message.");
            System.out.println("-> Offers that are optimal without message:");
            for (LyingOfferType smt : bestOffersWithoutMessage) {
                System.out.println("\t- value=" + noMessageOfferValue + "; offer to self=" + smt.offer() + "; " +
                        Arrays.toString(Chips.getBins(smt.offer(), game.getBinMaxChips())));
            }
            System.out.println("\t- Chosen is offer to self: " + bestLyingOfferType.offer() + "; " +
                    Arrays.toString(Chips.getBins(bestLyingOfferType.offer(), game.getBinMaxChips())));

            // choose offer with message
            bestLyingOfferType = bestOffers.get((int) (Math.random() * bestOffers.size()));
            System.out.println("-> Offers that are optimal with message are:");
            for (LyingOfferType something : bestOffers) {
                System.out.println("\t- value=" + tmpSelectOfferValue + ", offer to self=" + something.offer() + "; " +
                        Arrays.toString(Chips.getBins(something.offer(), game.getBinMaxChips())) +
                        ", loc=" + something.loc());
                if (something.loc() == game.getGoalPositionPlayer(this.getName())) {
                    lyingIsBetter = false;
                }
            }
            System.out.println("\t- Chosen is offer to self: " + bestLyingOfferType.offer() + "; " +
                    Arrays.toString(Chips.getBins(bestLyingOfferType.offer(), game.getBinMaxChips())) +
                    ", loc=" + bestLyingOfferType.loc());
            if (lyingIsBetter) {
                System.out.println("--- THERE IS A COMBINATION WHERE LYING IS BETTER THAN TELLING THE TRUTH. ---");
            }
        }

        bestOffer = bestLyingOfferType.offer();
        bestLoc = bestLyingOfferType.loc();

        if (utilityFunction[offerReceived] >= tmpSelectOfferValue &&
                utilityFunction[offerReceived] > utilityFunction[this.chips]) {
            // accept offerReceived as it is better than making a new offer or withdrawing
            bestOffer = offerReceived;
        } else if (utilityFunction[this.chips] >= utilityFunction[bestOffer] &&
                utilityFunction[this.chips] >= utilityFunction[offerReceived]) {
            // withdraw from negotiation
            bestOffer = this.chips;
        } else if (bestLoc != -1) { // else, make the best offer with possibly a message
            sendMessage(Messages.createLocationMessage(bestLoc));
        }
        System.out.println("Value of offer = " + getValue(bestOffer));
        return bestOffer;
    }

    private void addOffers(int loc) {
        double curValue;
        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
            if (loc != -1) { // TODO: make this more efficient?
                partnerModel.saveBeliefs();
                partnerModel.receiveMessage(Messages.createLocationMessage(loc));
                curValue = getValue(i);
                partnerModel.restoreBeliefs();
            } else {
                curValue = getValue(i);
            }

            if (curValue > tmpSelectOfferValue) {
                tmpSelectOfferValue = curValue;
                bestOffers = new ArrayList<>();
                bestOffers.add(new LyingOfferType(i, loc));
                thereIsBestOfferWithoutMessage = false;
            } else if (curValue == tmpSelectOfferValue) {
                bestOffers.add(new LyingOfferType(i, loc));
            }
        }
    }

    public void sendMessage(String message) {
        super.sendMessage(message);
        addMessage(message, false);
        this.game.sendMessage(message);
    }

    public boolean isCanLie() {
        return canLie;
    }
}