package results.createResults;

import lyingAgents.model.Game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetResults {

    String CSV_FILE_NAME = "results.csv";
    String SAVE_DIRECTORY = "results";

    private final List<ResultElement> data = new ArrayList<>();

    private List<String[]> dataString;

    public GetResults() {
        makeFolder();
    }

    private void makeFolder() {
        File saveDirectory = new File(SAVE_DIRECTORY);
        if (!saveDirectory.exists()) {
            boolean wasSuccessful = saveDirectory.mkdir();
            if (!wasSuccessful) System.out.println("Directory not made...");
        }
    }

    public void makeResults() {
        int cnt = 0;

        while (cnt++ < ResultSettings.NUM_REP) {
            System.out.println("--- Repetition " + cnt + " ---");
            for (int initTom : ResultSettings.initTomList) {
                for (int respTom : ResultSettings.respTomList) {
                    for (double initLR : ResultSettings.initLRList) {
                        for (double respLR : ResultSettings.respLRList) {
                            for (boolean initCanLie : ResultSettings.initCanLieList) {
                                for (boolean respCanLie : ResultSettings.respCanLieList) {
                                    if ((initCanLie && (initTom < 2)) || (respCanLie && (respTom < 2))) { // agents without 2nd order tom cannot lie
                                        System.out.println("\t  Skipping [" + initTom + ", " + respTom + ", " + initLR + ", " + respLR + ", " + initCanLie + ", " + respCanLie + "] Done;");
                                    } else {
                                        generateNewGame(initTom, respTom, initLR, respLR, initCanLie, respCanLie);
                                        try {
                                            writeExcel();
                                        } catch (IOException exception) {
                                            System.out.println("writing to excel did not succeed.");
                                        }
                                        System.out.println("\t[" + initTom + ", " + respTom + ", " + initLR + ", " + respLR + ", " + initCanLie + ", " + respCanLie + "] Done;");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Finished iteration " + cnt + ".\n");
        }
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private void generateNewGame(int initTom, int respTom, double initLR, double respLR, boolean initCanLie, boolean respCanLie) {
        long startTime = System.currentTimeMillis();
        Game game = new Game(initTom, respTom, initLR, respLR, initCanLie, respCanLie);
        // TODO: might only generate games where there is a pareto optimal outcome?
        for (int i = 0; i < ResultSettings.WARMUP_ROUNDS; i++) {
            game.playTillEnd();
            game.newRound();
        }

        game.newRound();
        game.playTillEnd();
        long endTime = System.currentTimeMillis();
        ResultElement resultElement = new ResultElement(game, (endTime - startTime) / 1000.0);
        data.add(resultElement);

        for (int i = 1; i < ResultSettings.KEEP_RESULTS_NR_ROUNDS; i++) {
            game.newRound();
            game.playTillEnd();
            resultElement = new ResultElement(game, -1);
            data.add(resultElement);
        }
    }

    public void writeExcel() throws IOException {
        this.dataString = new ArrayList<>();
        addHeaders();
        createDataString();
        File csvOutputFile = new File(SAVE_DIRECTORY + File.separator + CSV_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataString.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    private void addHeaders() {
        String[] cell_headers = new String[] {
                ResultSettings.initTom, ResultSettings.respTom,
                ResultSettings.initLR, ResultSettings.respLR,
                ResultSettings.initCanLie, ResultSettings.respCanLie,
                ResultSettings.initInitPoints, ResultSettings.respInitPoints,
                ResultSettings.initFinalPoints, ResultSettings.respFinalPoints,
                ResultSettings.initGain, ResultSettings.respGain,
                ResultSettings.nrOffers, ResultSettings.isPE, ResultSettings.timePassed};
        dataString.add(cell_headers);
    }

    private void createDataString() {
        String[] dataLine;

        for (ResultElement resultElement : data) {
            dataLine = new String[] {
                    String.valueOf(resultElement.getInitToM()),
                    String.valueOf(resultElement.getRespToM()),

                    String.valueOf(resultElement.getInitLR()),
                    String.valueOf(resultElement.getRespLR()),

                    String.valueOf(resultElement.isInitCanLie()),
                    String.valueOf(resultElement.isRespCanLie()),

                    String.valueOf(resultElement.getInitInitialPoints()),
                    String.valueOf(resultElement.getRespInitialPoints()),

                    String.valueOf(resultElement.getInitFinalPoints()),
                    String.valueOf(resultElement.getRespFinalPoints()),

                    String.valueOf(resultElement.getInitGain()),
                    String.valueOf(resultElement.getRespGain()),
                    String.valueOf(resultElement.getNrOffers()),
                    String.valueOf(resultElement.isPE()),
                    String.valueOf(resultElement.getTimePassed())
            };
            dataString.add(dataLine);
        }
    }
}
