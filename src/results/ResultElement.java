package results;

import utilities.Game;

public class ResultElement {

    private final int initToM;
    private final int respToM;

    private final double initLR;
    private final double respLR;

    private final int initFinalPoints;
    private final int respFinalPoints;

    private final int initInitialPoints;
    private final int respInitialPoints;

    private final int nrOffers;

    public ResultElement(Game game) {
        initToM = game.getInitiator().getOrderToM();
        initLR = game.getInitiator().getLearningSpeed();
        initFinalPoints = game.getInitiator().getFinalPoints();
        initInitialPoints = game.getInitiator().getInitialPoints();

        respToM = game.getResponder().getOrderToM();
        respLR = game.getResponder().getLearningSpeed();
        respFinalPoints = game.getResponder().getFinalPoints();
        respInitialPoints = game.getResponder().getInitialPoints();

        nrOffers = game.getNrOffers();
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
}
