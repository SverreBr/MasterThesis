package results.createResults;

import lyingAgents.model.Game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetResults {
    String SAVE_DIRECTORY = "tmp_results";

    private final List<ResultElement> data = new ArrayList<>();

    private List<String[]> dataString;

    private final String csvFileName;

    public GetResults(String csvFileName) {
        this.csvFileName = csvFileName;
        makeFolder();
    }

    private void makeFolder() {
        File saveDirectory = new File(SAVE_DIRECTORY);
        if (!saveDirectory.exists()) {
            boolean wasSuccessful = saveDirectory.mkdir();
            if (!wasSuccessful) System.out.println("Directory not made...");
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

    public void generateNewResults(Game game) {
        int i;
        ResultElement resultElement;
        long endTime, startTime;

        startTime = System.currentTimeMillis();
        for (i = 0; i < ResultSettings.WARMUP_ROUNDS; i++) {
            game.playTillEnd();
            game.newRound();
        }

        i = 0;
        do {
            do {
                game.newRound();
            } while (game.canAnyAgentReachTheirGP());
            game.playTillEnd();
            endTime = System.currentTimeMillis();
            resultElement = new ResultElement(game, (endTime - startTime) / 1000.0);
            data.add(resultElement);
            startTime = System.currentTimeMillis();
            i++;
        } while (i < ResultSettings.KEEP_RESULTS_NR_ROUNDS);
    }

    public void writeExcel() throws IOException {
        this.dataString = new ArrayList<>();
        addHeaders();
        createDataString();
        File csvOutputFile = new File(SAVE_DIRECTORY + File.separator + csvFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataString.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    private void addHeaders() {
        String[] cell_headers = new String[]{
                ResultSettings.initTom, ResultSettings.respTom,
                ResultSettings.initLR, ResultSettings.respLR,
                ResultSettings.initCanSendMessages, ResultSettings.respCanSendMessages,
                ResultSettings.initCanLie, ResultSettings.respCanLie,
                ResultSettings.initInitPoints, ResultSettings.respInitPoints,
                ResultSettings.initFinalPoints, ResultSettings.respFinalPoints,
                ResultSettings.initGain, ResultSettings.respGain,
                ResultSettings.initCanInitiallyReachGP, ResultSettings.respCanInitiallyReachGP,
                ResultSettings.initZeroToMProb, ResultSettings.respZeroToMProb,
                ResultSettings.initNumberOfMessagesSent, ResultSettings.respNumberOfMessagesSent,
                ResultSettings.initNumberOfTimesLied, ResultSettings.respNumberOfTimesLied,
                ResultSettings.initHighestValueParetoOutcome, ResultSettings.respHighestValueParetoOutcome,
                ResultSettings.initHighestValueStrictParetoOutcome, ResultSettings.respHighestValueStrictParetoOutcome,
                ResultSettings.highestSWStrictPE, ResultSettings.highestSWPE,
                ResultSettings.nrOffers,
                ResultSettings.outcomeIsStrictPE, ResultSettings.outcomeIsPE,
                ResultSettings.isBestSWFromStrictPE,
                ResultSettings.isNewOfferAccepted, ResultSettings.thereIsABetterOutcomeThanInitialSitu,
                ResultSettings.reachedMaxNumOffers, ResultSettings.timePassed};
        dataString.add(cell_headers);
    }

    private void createDataString() {
        String[] dataLine;

        for (ResultElement resultElement : data) {
            dataLine = new String[]{
                    String.valueOf(resultElement.getInitToM()),
                    String.valueOf(resultElement.getRespToM()),

                    String.valueOf(resultElement.getInitLR()),
                    String.valueOf(resultElement.getRespLR()),

                    String.valueOf(resultElement.isInitCanSendMessages()),
                    String.valueOf(resultElement.isRespCanSendMessages()),

                    String.valueOf(resultElement.isInitCanLie()),
                    String.valueOf(resultElement.isRespCanLie()),

                    String.valueOf(resultElement.getInitInitialPoints()),
                    String.valueOf(resultElement.getRespInitialPoints()),

                    String.valueOf(resultElement.getInitFinalPoints()),
                    String.valueOf(resultElement.getRespFinalPoints()),

                    String.valueOf(resultElement.getInitGain()),
                    String.valueOf(resultElement.getRespGain()),

                    String.valueOf(resultElement.isInitCanInitiallyReachGP()),
                    String.valueOf(resultElement.isRespCanInitiallyReachGP()),

                    String.valueOf(resultElement.getInitZeroToMProb()),
                    String.valueOf(resultElement.getRespZeroToMProb()),

                    String.valueOf(resultElement.getInitNumberOfMessagesSent()),
                    String.valueOf(resultElement.getRespNumberOfMessagesSent()),

                    String.valueOf(resultElement.getInitNumberOfTimesLied()),
                    String.valueOf(resultElement.getRespNumberOfTimesLied()),

                    String.valueOf(resultElement.getInitHighestValueParetoOutcome()),
                    String.valueOf(resultElement.getRespHighestValueParetoOutcome()),

                    String.valueOf(resultElement.getInitHighestValueStrictParetoOutcome()),
                    String.valueOf(resultElement.getRespHighestValueStrictParetoOutcome()),

                    String.valueOf(resultElement.getHighestSWStrictPE()),
                    String.valueOf(resultElement.getHighestSWPE()),

                    String.valueOf(resultElement.getNrOffers()),
                    String.valueOf(resultElement.isStrictPE()),
                    String.valueOf(resultElement.isPE()),
                    String.valueOf(resultElement.isBestSW()),
                    String.valueOf(resultElement.isNewOfferAccepted()),
                    String.valueOf(resultElement.isThereIsBetterOutcomeThanInitialSituForBothAgents()),
                    String.valueOf(resultElement.isReachedMaxNumOffers()),
                    String.valueOf(resultElement.getTimePassed())
            };
            dataString.add(dataLine);
        }
    }
}
