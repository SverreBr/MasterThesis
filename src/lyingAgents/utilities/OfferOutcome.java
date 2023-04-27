package lyingAgents.utilities;

public class OfferOutcome implements Comparable<OfferOutcome>{
    private final int offerForInit;
    private final int valueInit;
    private final int valueResp;

    public OfferOutcome(int offerForInit, int valueInit, int valueResp) {
        this.offerForInit = offerForInit;
        this.valueInit = valueInit;
        this.valueResp = valueResp;
    }

    public int getOfferForInit() {
        return offerForInit;
    }

    public int getValueInit() {
        return valueInit;
    }

    public int getValueResp() {
        return valueResp;
    }

    public int getSocialWelfare() {
        return valueInit + valueResp;
    }

    @Override
    public int compareTo(OfferOutcome o) {
        return getSocialWelfare() - o.getSocialWelfare();
    }
}
