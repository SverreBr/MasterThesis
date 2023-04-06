package lyingAgents.utilities;

public class OfferOutcome {
    private final int offer;
    private final int valueInit;
    private final int valueResp;

    public OfferOutcome(int offer, int valueInit, int valueResp) {
        this.offer = offer;
        this.valueInit = valueInit;
        this.valueResp = valueResp;
    }

    public int getOffer() {
        return offer;
    }

    public int getValueInit() {
        return valueInit;
    }

    public int getValueResp() {
        return valueResp;
    }
}
