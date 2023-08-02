package lyingAgents.utilities;

public class RandomNumGen {
    private final int[] numbers;
    private final double[] prob;

    public RandomNumGen(int[] numbers, double[] prob) {
        this.numbers = numbers;
        this.prob = prob;
    }

    public int random() {
        double p = Math.random();
        double sum = 0.0;
        int i = 0;
        while (sum + Settings.EPSILON < p) {
            sum += prob[i];
            i++;
        }
        return numbers[i - 1];
    }

}
