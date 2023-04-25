package results.createResults;

import lyingAgents.model.Game;
import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.OfferOutcome;

import java.util.List;

public class ResultElement {

    private final int initToM;
    private final int respToM;

    private final double initLR;
    private final double respLR;

    private final boolean initCanLie;
    private final boolean respCanLie;


    private final int initFinalPoints;
    private final int respFinalPoints;

    private final int initInitialPoints;
    private final int respInitialPoints;

    private final int nrOffers;
    private boolean isPE;
    private boolean isBestSW;
    private boolean isNegotiationSuccess;
    private final boolean thereIsBetterOutcome;
    private final boolean reachedMaxNumOffers;
    private final double timePassed;

    public ResultElement(Game game, double timePassed) {
        PlayerLying init = game.getInitiator();
        PlayerLying resp = game.getResponder();

        initToM = init.getOrderToM();
        initLR = init.getLearningSpeed();
        initCanLie = init.isCanLie();
        initFinalPoints = init.getFinalPoints();
        initInitialPoints = init.getInitialPoints();

        respToM = resp.getOrderToM();
        respLR = resp.getLearningSpeed();
        respCanLie = resp.isCanLie();
        respFinalPoints = resp.getFinalPoints();
        respInitialPoints = resp.getInitialPoints();

        nrOffers = game.getTotalNrOffersMade();

        List<OfferOutcome> peList = game.getStrictParetoOutcomes();
        thereIsBetterOutcome = !peList.isEmpty();
        calcIfNegotiationIsSuccess(peList.size(), game);
        calcIfOutcomeIsPE(peList, game);
        calcIfOutcomeIsBestSW(peList, game);
        reachedMaxNumOffers = game.isReachedMaxNumOffers();

        this.timePassed = timePassed;
    }

    private void calcIfOutcomeIsPE(List<OfferOutcome> peList, Game game) {
        isPE = true;
        if (peList.size() == 0) return;

        int respUtil = game.getResponder().getUtilityValue();
        int initUtil = game.getInitiator().getUtilityValue();

        for (OfferOutcome outcome : peList) {
            if ((outcome.getValueInit() > initUtil) && (outcome.getValueResp() > respUtil)) {
                isPE = false;
                break;
            }
        }
    }

    private void calcIfOutcomeIsBestSW(List<OfferOutcome> peList, Game game) {
        isBestSW = true;
        if (peList.size() == 0) return;
        int highestSW = peList.get(0).getSocialWelfare();
        for (OfferOutcome outcome : peList) {
            highestSW = Math.max(highestSW, outcome.getSocialWelfare());
        }
        int sw = game.getResponder().getUtilityValue() + game.getInitiator().getUtilityValue();
        isBestSW = (highestSW == sw);
    }

    private void calcIfNegotiationIsSuccess(int numBetterOutcomes, Game game) {
        isNegotiationSuccess = true;
        if (numBetterOutcomes == 0) return;
        int initInitialChips = game.getInitiator().getInitialChips();
        int initFinalChips = game.getInitiator().getChips();
        isNegotiationSuccess = (initInitialChips != initFinalChips);
    }

    public int getInitGain() {
        return initFinalPoints - initInitialPoints;
    }

    public int getRespGain() {
        return respFinalPoints - respInitialPoints;
    }

    // GETTERS
    public int getInitToM() {
        return initToM;
    }

    public int getRespToM() {
        return respToM;
    }

    public double getInitLR() {
        return initLR;
    }

    public double getRespLR() {
        return respLR;
    }

    public int getInitFinalPoints() {
        return initFinalPoints;
    }

    public int getRespFinalPoints() {
        return respFinalPoints;
    }

    public int getInitInitialPoints() {
        return initInitialPoints;
    }

    public int getRespInitialPoints() {
        return respInitialPoints;
    }

    public int getNrOffers() {
        return nrOffers;
    }

    public boolean isInitCanLie() {
        return initCanLie;
    }

    public boolean isRespCanLie() {
        return respCanLie;
    }

    public boolean isPE() {
        return isPE;
    }

    public double getTimePassed() {
        return timePassed;
    }

    public boolean isBestSW() {
        return isBestSW;
    }

    public boolean isNegotiationSuccess() {
        return isNegotiationSuccess;
    }

    public boolean isThereIsBetterOutcome() {
        return thereIsBetterOutcome;
    }

    public boolean isReachedMaxNumOffers() {
        return reachedMaxNumOffers;
    }
}
