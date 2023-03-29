package lyingAgents.utilities;

/**
 * Chips class: contains all methods that are used to calculate with chips.
 */
public class Chips {

    /**
     * Returns the sum of the positive elements
     */
    public static int getPositiveAmount(int[] differences) {
        int acc = 0;
        for (int difference : differences) {
            if (difference > 0) {
                acc += difference;
            }
        }
        return acc;
    }

    /**
     * Returns the sum of the negative elements
     */
    public static int getNegativeAmount(int[] differences) {
        int acc = 0;
        for (int difference : differences) {
            if (difference < 0) {
                acc -= difference;
            }
        }
        return acc;
    }

    /**
     * Returns the bin differences of int offers
     *
     * @param index1 first offer
     * @param index2 second offer
     * @param binMax maximum amount per bin
     * @return differences per bin
     */
    public static int[] getDifference(int index1, int index2, int[] binMax) {
        int[] bins1 = getBins(index1, binMax);
        int[] bins2 = getBins(index2, binMax);
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            bins1[i] -= bins2[i];
        }
        return bins1;
    }

    /**
     * Gets the complementary index of the given offer. That is, input + output = binMax
     *
     * @param index  offer
     * @param binMax maximum amount per bin
     * @return complementary offer
     */
    public static int invert(int index, int[] binMax) {
        int[] bins = getBins(index, binMax);
        for (int i = 0; i < bins.length; ++i) {
            bins[i] = binMax[i] - bins[i];
        }
        return getIndex(bins, binMax);

    }

    /**
     * Converts an offer specified per bin to an offer as an index
     *
     * @param bins   offer as bins
     * @param binMax maximum amount per bin
     * @return offer as index
     */
    public static int getIndex(int[] bins, int[] binMax) {
        int outCode = bins[Settings.CHIP_DIVERSITY - 1];
        for (int i = Settings.CHIP_DIVERSITY - 2; i >= 0; i--) {
            outCode = outCode * (binMax[i] + 1) + bins[i];
        }
        return outCode;
    }

    /**
     * Calculates the number of chips in chip array
     *
     * @param chips the chip array
     * @return integer that represents the total number of chips.
     */
    public static int getNrChips(int[] chips) {
        int sum = 0;
        for (int chip : chips)
            sum += chip;
        return sum;
    }

    /**
     * Converts an offer as index to an offer as bins
     *
     * @param index  offer as index
     * @param binMax maximum amount per bin
     * @return offer as bins
     */
    public static int[] getBins(int index, int[] binMax) {
        int[] bins = new int[Settings.CHIP_DIVERSITY];
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            bins[i] = index % (binMax[i] + 1);
            index /= (binMax[i] + 1);
        }
        return bins;
    }

    /**
     * Makes an empty bin
     *
     * @return empty bin initializes with zeros
     */
    public static int[] makeNewChipBin() {
        int[] newChipBin = new int[Settings.CHIP_DIVERSITY];
        for (int i = 0; i < Settings.CHIP_DIVERSITY; i++) {
            newChipBin[i] = 0;
        }
        return newChipBin;
    }
}
