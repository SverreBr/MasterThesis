package lyingAgents.model.player;

import lyingAgents.utilities.Settings;

public class OfferType {
    private final int offer;
    private final int loc;
    private double value;
    public OfferType(int offer, double value, int loc) {
        this.offer = offer;
        this.value = value;
        this.loc = loc;
    }

    public int getOffer() {
        return offer;
    }

    public int getLoc() {
        return loc;
    }

    public double getValue() {
        return value;
    }
}
