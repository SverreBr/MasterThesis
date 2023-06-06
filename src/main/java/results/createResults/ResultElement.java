package results.createResults;

import lyingAgents.model.Game;
import lyingAgents.model.player.PlayerLying;
import lyingAgents.utilities.Chips;
import lyingAgents.utilities.MiscFunc;
import lyingAgents.utilities.OfferOutcome;
import lyingAgents.utilities.Settings;

import java.util.HashMap;
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

    private final double initZeroToMProb;
    private final double respZeroToMProb;

    private final int initNumberOfTimesLied;
    private final int respNumberOfTimesLied;

    private final int initNumberOfMessagesSent;
    private final int respNumberOfMessagesSent;

    private final int initHighestValueStrictParetoOutcome;
    private final int respHighestValueStrictParetoOutcome;

    private final int initHighestValueParetoOutcome;
    private final int respHighestValueParetoOutcome;

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
        initFinalPoints = init.getUtilityValue();
        initInitialPoints = init.getInitialPoints();
        initCanInitiallyReachGP = game.getBoard().canReachGP(Settings.STARTING_POSITION,
                Chips.getBins(init.getInitialChips(), game.getBinMaxChips()), game.getGoalPositionPointPlayer(Settings.INITIATOR_NAME));
        initZeroToMProb = init.getPROB_TOM0_SEND_MESSAGE();
        initNumberOfTimesLied = init.getNumberOfTimesLied();
        initNumberOfMessagesSent = init.getNumberOfMessagesSent();


        respToM = resp.getOrderToM();
        respLR = resp.getLearningSpeed();
        respCanSendMessages = resp.isCanSendMessages();
        respCanLie = resp.isCanMakeFalseStatements();
        respFinalPoints = resp.getUtilityValue();
        respInitialPoints = resp.getInitialPoints();
        respCanInitiallyReachGP = game.getBoard().canReachGP(Settings.STARTING_POSITION,
                Chips.getBins(resp.getInitialChips(), game.getBinMaxChips()), game.getGoalPositionPointPlayer(Settings.RESPONDER_NAME));
        respZeroToMProb = resp.getPROB_TOM0_SEND_MESSAGE();
        respNumberOfTimesLied = resp.getNumberOfTimesLied();
        respNumberOfMessagesSent = resp.getNumberOfMessagesSent();

        nrOffers = game.getTotalNrOffersMade();
        List<OfferOutcome> strictPEList = game.getStrictParetoOutcomes();
        thereIsBetterOutcomeThanInitialSituForBothAgents = !strictPEList.isEmpty();
        isNewOfferAccepted = MiscFunc.isNewOfferAccepted(game);
        isStrictPE = MiscFunc.calcIfOutcomeIsStrictPE(strictPEList, game);
        isBestSW = MiscFunc.calcIfOutcomeIsBestSW(strictPEList, game);
        reachedMaxNumOffers = game.isReachedMaxNumOffers();

        HashMap<String, Integer> map = MiscFunc.getHighestValue(strictPEList);
        initHighestValueStrictParetoOutcome = map.get(Settings.INITIATOR_NAME);
        respHighestValueStrictParetoOutcome = map.get(Settings.RESPONDER_NAME);

        List<OfferOutcome> PEList = game.getParetoOutcomes();
        map = MiscFunc.getHighestValue(PEList);
        initHighestValueParetoOutcome = map.get(Settings.INITIATOR_NAME);
        respHighestValueParetoOutcome = map.get(Settings.RESPONDER_NAME);

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

    public double getInitZeroToMProb() {
        return initZeroToMProb;
    }

    public double getRespZeroToMProb() {
        return respZeroToMProb;
    }

    public int getInitHighestValueStrictParetoOutcome() {
        return initHighestValueStrictParetoOutcome;
    }

    public int getRespHighestValueStrictParetoOutcome() {
        return respHighestValueStrictParetoOutcome;
    }

    public int getInitHighestValueParetoOutcome() {
        return initHighestValueParetoOutcome;
    }

    public int getRespHighestValueParetoOutcome() {
        return respHighestValueParetoOutcome;
    }

    public int getInitNumberOfTimesLied() {
        return initNumberOfTimesLied;
    }

    public int getRespNumberOfTimesLied() {
        return respNumberOfTimesLied;
    }

    public int getInitNumberOfMessagesSent() {
        return initNumberOfMessagesSent;
    }

    public int getRespNumberOfMessagesSent() {
        return respNumberOfMessagesSent;
    }
}
