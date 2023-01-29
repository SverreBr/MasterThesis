package alternatingOffers;

import utilities.Settings;

import java.util.Set;

public abstract class Player {

    /**
     * Fields
     */
    private int[] tokens;
    public final String name;
    public final Settings settings;

//    private final

    public Player (String namePlayer, Settings settings) {
        name = namePlayer;
        this.settings = settings;
    }

    /**
     * Give the player tokens
     */
    public void obtainTokens(int [] initTokens) {
        tokens = initTokens;
    }

    public int getNumTokens() {
        return tokens.length;
    }

    public int[] getTokens() {
        return tokens;
    }

//    public int calculateCurrentPoints() {
//        continue
//    }
}
