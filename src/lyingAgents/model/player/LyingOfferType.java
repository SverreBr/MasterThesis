package lyingAgents.model.player;

public class LyingOfferType {
    private final int offer;
    private final int loc;
    public LyingOfferType(int offer, int loc) {
        this.offer = offer;
        this.loc = loc;
    }

    public int getOffer() {
        return offer;
    }

    public int getLoc() {
        return loc;
    }
}
