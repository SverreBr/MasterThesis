package lyingAgents.model.player;

/**
 * Record to store an offer and a location message
 *
 * @param offer The offer to make
 * @param loc   The location to announce to the trading partner
 */
public record LyingOfferType(int offer, int loc) {
}
