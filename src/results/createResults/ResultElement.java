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
    private final boolean isPE;
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

        nrOffers = game.getNrOffers();
        isPE = calcIfOutcomeIsMaxPE(game);
        this.timePassed = timePassed;
    }
    private boolean calcIfOutcomeIsMaxPE(Game game) {
        List<OfferOutcome> outcomeList = game.getParetoOutcomes();
        if (outcomeList.size() == 0) return true;
        int highestSW = outcomeList.get(0).getSocialWelfare();
        for (OfferOutcome outcome : outcomeList) {
            highestSW = Math.max(highestSW, outcome.getSocialWelfare());
        }
        int sw = game.getResponder().getUtilityValue() + game.getInitiator().getUtilityValue();
        return highestSW == sw;
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
}
