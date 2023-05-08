package results;

import lyingAgents.utilities.exceptions.ForbiddenCharacterException;
import lyingAgents.utilities.exceptions.NameTooLongException;
import lyingAgents.utilities.exceptions.NameTooShortException;
import lyingAgents.utilities.Settings;
import results.createResults.GetResults;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainResults {

    private static String csvFileName = "results.csv";

    public static void main(String[] args) {
        handleArguments(args);
        createResults();
    }

    private static void handleArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String option = args[i];

            if (option.equals("-id") && (i + 1 < args.length)) {
                csvFileName = "results_" + args[i + 1] + ".csv";
                try {
                    validate(csvFileName);
                } catch (NameTooLongException | ForbiddenCharacterException | NameTooShortException ex) {
                    System.out.println("File name not correct.");
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

    private static void createResults() {
        GetResults getResults = new GetResults(csvFileName);
        getResults.makeResults();
        try {
            getResults.writeExcel();
        } catch (IOException exception) {
            System.out.println("Writing to Excel did not work.");
        }
    }

    private static void printHelpMessage() {
        System.out.println("Usage: java MainResults [options]");
        System.out.println("Options:");
        System.out.println("  -id <string>    ID of the results that will be used as output file (results_ + id + .csv)");
        System.out.println("  -h, --help      Print this message and exit");
    }

}
