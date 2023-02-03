package utilities;

/**
 * PlayerToM: class for the theory of mind of the agent
 */
public class PlayerToM extends Player {

    private double[][] beliefsOfferType;

    /**
     * Constructor
     *
     * @param playerName name of the agent
     * @param game       the model of the game
     */
    public PlayerToM(String playerName, Game game) {
        super(playerName, game);
    }

    public void setupBeliefs() {
        this.beliefsOfferType = new double[1][1];
//        for (int i = 0; i < this.)
    }
}
