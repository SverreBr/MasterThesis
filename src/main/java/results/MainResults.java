package results;

import lyingAgents.model.Game;
import lyingAgents.utilities.exceptions.ForbiddenCharacterException;
import lyingAgents.utilities.exceptions.NameTooLongException;
import lyingAgents.utilities.exceptions.NameTooShortException;
import lyingAgents.utilities.Settings;
import results.createResults.GetResults;
import results.createResults.ResultSettings;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainResults {

    private static String csvFileName = "results.csv";
    private static String experimentName = "main";
    private static final List<String> experiment_options = Arrays.asList("main", "determine_prob", "determine_prob_compare");

    private static final String FILE_ID = "-file_id";
    private static final String EXPERIMENT_ID = "-experiment";

    public static void main(String[] args) {
        handleArguments(args);
        createResults();
    }

    private static void handleArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String option = args[i];

            if (option.equals(FILE_ID) && (i + 1 < args.length)) {
                csvFileName = "results_" + args[i + 1] + ".csv";
                try {
                    validate(csvFileName);
                } catch (NameTooLongException | ForbiddenCharacterException | NameTooShortException ex) {
                    System.out.println("File name '" + csvFileName + "' not correct.");
                    System.exit(-1);
                }
            } else if (option.equals(EXPERIMENT_ID) && (i + 1 < args.length)) {
                experimentName = args[i + 1];
                if (!experiment_options.contains(experimentName)) {
                    System.out.println("Experiment id is not implemented");
                    System.out.println(-1);
                }
            } else if (option.equals("-h") || option.equals("--help")) {
                printHelpMessage();
                System.exit(0);
            }
        }
    }

    private static void validate(String filename) throws NameTooLongException, ForbiddenCharacterException, NameTooShortException {

        if (filename.length() > Settings.MAX_FILE_NAME) throw new NameTooLongException(filename);
        if (filename.length() < Settings.MIN_FILE_NAME) throw new NameTooShortException(filename);

        Pattern allowedCharacters = Pattern.compile("[^A-Za-z0-9-_.]");
        Matcher match = allowedCharacters.matcher(filename);
        boolean matchBoolean = match.find();
        if (matchBoolean) throw new ForbiddenCharacterException(filename);
    }

    private static void printHelpMessage() {
        System.out.println("Usage: java MainResults [options]");
        System.out.println("Options:");
        System.out.println("  " + FILE_ID + " <string>    ID of the results file that will be used as output file (results_ + id + .csv), default = " + csvFileName);
        System.out.println("  " + EXPERIMENT_ID + " <string>    ID of the experiment, default = " + experimentName + ", options = " + experiment_options);
        System.out.println("  -h, --help      Print this message and exit");
    }

    private static void createResults() {
        if (experimentName.equals("main")) {
            performMainExperiment();
        } else if (experimentName.equals("determine_prob")) {
            performDetermineProbExperiment();
        } else if (experimentName.equals("determine_prob_compare")) {
            performDetermineProbCompareExperiment();
        } else {
            System.out.println("No results obtained as experiment name not implemented");
        }
    }

    private static void performMainExperiment() {
        GetResults getResults = new GetResults(csvFileName);
        double zeroToMProb = 0.25;

        int cnt = 0;
        while (cnt++ < ResultSettings.NUM_REP) {
            System.out.println("--- Start Repetition " + cnt + " ---");
            for (int initTom : ResultSettings.initTomList) {
                for (int respTom : ResultSettings.respTomList) {
                    for (boolean initCanSendMessages : ResultSettings.initCanSendMessagesList) {
                        for (boolean respCanSendMessages : ResultSettings.respCanSendMessagesList) {
                            for (boolean initCanLie : ResultSettings.initCanLieList) {
                                if ((!initCanSendMessages && initCanLie)) continue;
                                if ((initTom == 0) && !initCanLie) continue;
                                for (boolean respCanLie : ResultSettings.respCanLieList) {
                                    if (!respCanSendMessages && respCanLie) continue;
                                    if ((respTom == 0) && !respCanLie) continue;

                                    Game game = new Game(initTom, respTom, Settings.STANDARD_LR, Settings.STANDARD_LR, initCanLie, respCanLie, initCanSendMessages, respCanSendMessages);
                                    game.getInitiator().setPROB_TOM0_SEND_MESSAGE(zeroToMProb);
                                    game.getResponder().setPROB_TOM0_SEND_MESSAGE(zeroToMProb);
                                    getResults.generateNewResults(game);

                                    System.out.println("\t[i_tom=" + initTom + ", r_tom=" + respTom +
                                            ", i_mess=" + initCanSendMessages + ", r_mess=" + respCanSendMessages +
                                            ", i_lie=" + initCanLie + ", r_lie=" + respCanLie + "] Done;");
                                }
                            }
                        }
                    }
                }
            }
            try {
                getResults.writeExcel();
            } catch (IOException exception) {
                System.out.println("!!! WRITING TO EXCEL DID NOT SUCCEED !!!");
            }
            System.out.println("################### Finished iteration " + cnt + " ###################\n");
        }
    }

    private static void performDetermineProbExperiment() {
        GetResults getResults = new GetResults(csvFileName);
        boolean initCanSendMessages = true;
        boolean respCanSendMessages = true;

        int cnt = 0;
        while (cnt++ < ResultSettings.NUM_REP) {
            System.out.println("--- Start Repetition " + cnt + " ---");
            for (int initTom : ResultSettings.initTomList) {
                for (int respTom : ResultSettings.respTomList) {
                    if ((initTom != 0) && (respTom != 0)) continue;
                    for (double zeroToMProb : ResultSettings.tom0probList) {
                        for (boolean initCanLie : ResultSettings.initCanLieList) {
                            if ((initTom == 0) && !initCanLie) continue;
                            for (boolean respCanLie : ResultSettings.respCanLieList) {
                                if ((respTom == 0) && !respCanLie) continue;

                                Game game = new Game(initTom, respTom, Settings.STANDARD_LR, Settings.STANDARD_LR, initCanLie, respCanLie, initCanSendMessages, respCanSendMessages);
                                game.getInitiator().setPROB_TOM0_SEND_MESSAGE(zeroToMProb);
                                game.getResponder().setPROB_TOM0_SEND_MESSAGE(zeroToMProb);

                                getResults.generateNewResults(game);
                                System.out.println("\t[i_tom=" + initTom + ", r_tom=" + respTom +
                                        ", prob=" + zeroToMProb +
                                        ", i_mess=" + initCanSendMessages + ", r_mess=" + respCanSendMessages +
                                        ", i_lie=" + initCanLie + ", r_lie=" + respCanLie + "] Done;");
                            }
                        }
                    }
                }
            }
            try {
                getResults.writeExcel();
            } catch (IOException exception) {
                System.out.println("!!! WRITING TO EXCEL DID NOT SUCCEED !!!");
            }
            System.out.println("################### Finished iteration " + cnt + " ###################\n");
        }
    }

    private static void performDetermineProbCompareExperiment() {
        GetResults getResults = new GetResults(csvFileName);
        boolean initCanSendMessages = true;
        boolean respCanSendMessages = true;
        double zeroToMProb = 0.0;

        int cnt = 0;
        while (cnt++ < ResultSettings.NUM_REP) {
            System.out.println("--- Start Repetition " + cnt + " ---");
            for (int initTom : ResultSettings.initTomList) {
                for (int respTom : ResultSettings.respTomList) {
                    if ((initTom == 0) && (respTom == 0)) continue;
                    for (boolean initCanLie : ResultSettings.initCanLieList) {
                        if ((initTom == 0) && !initCanLie) continue;
                        for (boolean respCanLie : ResultSettings.respCanLieList) {
                            if ((respTom == 0) && !respCanLie) continue;

                            Game game = new Game(initTom, respTom, Settings.STANDARD_LR, Settings.STANDARD_LR, initCanLie, respCanLie, initCanSendMessages, respCanSendMessages);
                            game.getInitiator().setPROB_TOM0_SEND_MESSAGE(zeroToMProb);
                            game.getResponder().setPROB_TOM0_SEND_MESSAGE(zeroToMProb);

                            getResults.generateNewResults(game);
                            System.out.println("\t[i_tom=" + initTom + ", r_tom=" + respTom +
                                    ", prob=" + zeroToMProb +
                                    ", i_mess=" + initCanSendMessages + ", r_mess=" + respCanSendMessages +
                                    ", i_lie=" + initCanLie + ", r_lie=" + respCanLie + "] Done;");
                        }
                    }
                }
            }
            try {
                getResults.writeExcel();
            } catch (IOException exception) {
                System.out.println("!!! WRITING TO EXCEL DID NOT SUCCEED !!!");
            }
            System.out.println("################### Finished iteration " + cnt + " ###################\n");
        }
    }
}
