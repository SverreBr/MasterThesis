package results.createResults;

import java.util.Arrays;
import java.util.List;

public class ResultSettings {

    public static final String initTom = "init_tom";
    public static final String initLR = "init_lr";
    public static final String initCanLie = "init_canLie";
    public static final String initInitPoints = "init_initPoints";
    public static final String initFinalPoints = "init_finalPoints";
    public static final String initGain = "init_gain";
    public static final String respTom = "resp_tom";
    public static final String respLR = "resp_lr";
    public static final String respCanLie = "resp_canLie";
    public static final String respInitPoints = "resp_initPoints";
    public static final String respFinalPoints = "resp_finalPoints";
    public static final String respGain = "resp_gain";
    public static final String nrOffers = "nr_offers";
    public static final String outcomeIsPE = "outcome_is_PE";
    public static final String isBestSW = "is_best_sw_outcome";
    public static final String isNegotiationSuccess = "negotiation_success";
    public static final String timePassed = "passed_time";
    public static final String thereIsABetterOutcome = "there_is_better_outcome";
    public static final String reachedMaxNumOffers = "reached_max_num_offers";
    public static final List<Integer> initTomList = Arrays.asList(0, 1, 2);
    public static final List<Integer> respTomList = Arrays.asList(0, 1, 2);
    public static final List<Double> initLRList = List.of(0.5);  // TODO: maybe make this normally distributed?
    public static final List<Double> respLRList = List.of(0.5);
    public static final List<Boolean> initCanLieList = Arrays.asList(true, false);
    public static final List<Boolean> respCanLieList = Arrays.asList(true, false);
    public static final int NUM_REP = 1;
    public static final int WARMUP_ROUNDS = 100;
    public static final int KEEP_RESULTS_NR_ROUNDS = 20;
}
