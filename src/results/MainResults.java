package results;

import results.createResults.GetResults;

import java.io.IOException;

public class MainResults {
    public static void main(String[] args) {
        GetResults getResults = new GetResults();
        getResults.makeResults();
        try {
            getResults.writeExcel();
        } catch (IOException exception) {
            System.out.println("Did not work.");
        }
    }
}
