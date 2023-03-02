package results;

import java.util.Arrays;
import java.util.List;

public class ResultSettings {
    public static final List<String> CELL_HEADERS = Arrays.asList(
            "initiator_tom", "responder_tom",
            "initiator_lr", "responder_lr",
            "initiator_initpoints", "responder_initpoints",
            "initiator_finalpoints", "responder_finalpoints",
            "initiator_gain", "responder_gain",
            "nr_offers");
    public static final List<Integer> initTomList = Arrays.asList(0, 1, 2);
    public static final List<Integer> respTomList = Arrays.asList(0, 1, 2);
    public static final List<Double> initLRList = Arrays.asList(0.1, 0.5, 0.9);
    public static final List<Double> respLRList = Arrays.asList(0.1, 0.5, 0.9);
    public static final int NUM_REP = 2;
    public static final int WARMUP_ROUNDS = 1;
    public static final int KEEP_RESULTS_NR_ROUNDS = 1;
}
