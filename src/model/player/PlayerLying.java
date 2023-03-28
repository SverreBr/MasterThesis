package model.player;

import model.Game;
import utilities.Chips;
import utilities.Messages;
import utilities.Settings;

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
            System.out.println("\n" + getName() + ": Value offer received= " + utilityFunction[offerReceived]);
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
        addOffers(-1, offerReceived);  // send no message

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
            if (lyingIsBetter) System.out.println("--- THERE IS A COMBINATION WHERE LYING IS BETTER THAN TELLING THE TRUTH. ---");
        }

        bestOffer = bestLyingOfferType.offer();
        bestLoc = bestLyingOfferType.loc();

        System.out.println(getName() + ": Offer=" + Arrays.toString(Chips.getBins(bestOffer, game.getBinMaxChips())) +
                "; Value of offer = " + Settings.PRINT_DF.format(getValue(bestOffer)) +
                "; tmpSelectOfferValue = " + Settings.PRINT_DF.format(tmpSelectOfferValue));
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
        } else if (bestLoc != -1) { // else, make the best offer with possibly a message
            sendMessage(Messages.createLocationMessage(bestLoc));
            System.out.println("Offer value after sending message: " + Settings.PRINT_DF.format(getValue(bestOffer)));
        }

        return bestOffer;
    }

    private void addOffers(int loc, int offerReceived) {
        double curValue;
        boolean saveHasSentMessage = this.hasSentMessage;

        for (int i = 0; i < utilityFunction.length; i++) {  // loop over offers
//            if (i == offerReceived) {
//                // TODO: If you make the same offer as you received, you accept it?
//                curValue = utilityFunction[offerReceived];
//            } else
            if (loc != -1) { // TODO: make this more efficient?
                partnerModel.saveBeliefs();
                partnerModel.receiveMessage(Messages.createLocationMessage(loc));
                curValue = getValue(i);
                partnerModel.restoreBeliefs();
                this.setHasSentMessage(saveHasSentMessage);
            } else {
                curValue = getValue(i);
            }

            if (curValue - Settings.EPSILON > tmpSelectOfferValue) {
                tmpSelectOfferValue = curValue;
                bestOffers = new ArrayList<>();
                bestOffers.add(new LyingOfferType(i, loc));
                thereIsBestOfferWithoutMessage = false;
            } else if ((curValue >= tmpSelectOfferValue - Settings.EPSILON) &&
                    (curValue <= tmpSelectOfferValue + Settings.EPSILON)) {
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