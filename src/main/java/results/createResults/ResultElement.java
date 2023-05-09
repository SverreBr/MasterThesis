package results.createResults;

import lyingAgents.model.Game;
import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.utilities.OfferOutcome;
import lyingAgents.utilities.Settings;

import java.util.List;

public class ResultElement {

    private final int initToM;
    private final int respToM;

    private final double initLR;
    private final double respLR;

    private final boolean initCanSendMessages;
    private final boolean respCanSendMessages;

    private final boolean initCanLie;
    private final boolean respCanLie;


    private final int initFinalPoints;
    private final int respFinalPoints;

    private final int initInitialPoints;
    private final int respInitialPoints;

    private final boolean initCanInitiallyReachGP;
    private final boolean respCanInitiallyReachGP;

    private final int nrOffers;
    private final boolean isStrictPE;
    private final boolean isBestSW;
    private final boolean isNewOfferAccepted;
    private final boolean thereIsBetterOutcomeThanInitialSituForBothAgents;
    private final boolean reachedMaxNumOffers;
    private final double timePassed;

    public ResultElement(Game game, double timePassed) {
        PlayerLying init = game.getInitiator();
        PlayerLying resp = game.getResponder();

        initToM = init.getOrderToM();
        initLR = init.getLearningSpeed();
        initCanLie = init.isCanMakeFalseStatements();
        initCanSendMessages = init.isCanSendMessages();
        initFinalPoints = init.getFinalPoints();
        initInitialPoints = init.getInitialPoints();
        initCanInitiallyReachGP = game.getBoard().canReachGP(Settings.STARTING_POSITION, init.getChipsBin(), game.getGoalPositionPointPlayer(Settings.INITIATOR_NAME));

        respToM = resp.getOrderToM();
        respLR = resp.getLearningSpeed();
        respCanSendMessages = resp.isCanSendMessages();
        respCanLie = resp.isCanMakeFalseStatements();
        respFinalPoints = resp.getFinalPoints();
        respInitialPoints = resp.getInitialPoints();
        respCanInitiallyReachGP = game.getBoard().canReachGP(Settings.STARTING_POSITION, resp.getChipsBin(), game.getGoalPositionPointPlayer(Settings.RESPONDER_NAME));

        nrOffers = game.getTotalNrOffersMade();

        List<OfferOutcome> peList = game.getStrictParetoOutcomes();
        thereIsBetterOutcomeThanInitialSituForBothAgents = !peList.isEmpty();
        isNewOfferAccepted = MiscFunc.isNewOfferAccepted(game);
        isStrictPE = MiscFunc.calcIfOutcomeIsStrictPE(peList, game);
        isBestSW = MiscFunc.calcIfOutcomeIsBestSW(peList, game);
        reachedMaxNumOffers = game.isReachedMaxNumOffers();

        this.timePassed = timePassed;
    }

    public int getInitGain() {
        return initFinalPoints - initInitialPoints;
    }

    public int getRespGain() {
        return respFinalPoints - respInitialPoints;
    }

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

    public boolean isStrictPE() {
        return isStrictPE;
    }

    public double getTimePassed() {
        return timePassed;
    }

    public boolean isBestSW() {
        return isBestSW;
    }

    public boolean isNewOfferAccepted() {
        return isNewOfferAccepted;
    }

    public boolean isThereIsBetterOutcomeThanInitialSituForBothAgents() {
        return thereIsBetterOutcomeThanInitialSituForBothAgents;
    }

    public boolean isReachedMaxNumOffers() {
        return reachedMaxNumOffers;
    }

    public boolean isInitCanSendMessages() {
        return initCanSendMessages;
    }

    public boolean isRespCanSendMessages() {
        return respCanSendMessages;
    }

    public boolean isInitCanInitiallyReachGP() {
        return initCanInitiallyReachGP;
    }

    public boolean isRespCanInitiallyReachGP() {
        return respCanInitiallyReachGP;
    }
}
